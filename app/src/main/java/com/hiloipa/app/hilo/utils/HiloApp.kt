package com.hiloipa.app.hilo.utils

import android.content.*
import android.net.ConnectivityManager
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.hiloipa.app.hilo.BuildConfig
import com.hiloipa.app.hilo.models.responses.UserData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.*
import okio.Buffer
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by eduardalbu on 31.01.2018.
 */
class HiloApp : MultiDexApplication() {

    private lateinit var preferences: SharedPreferences

    companion object {
        lateinit var instance: HiloApp
        lateinit var retrofit: Retrofit
        lateinit var userData: UserData
        const val PROVIDER_AUTHORITY = "com.hiloipa.app.hilo.provider"
        val CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
        val networkState: PublishSubject<Boolean> = PublishSubject.create()
        fun api(): HiloAPI = retrofit.create(HiloAPI::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        preferences = getSharedPreferences("hilo_app_prefs", Context.MODE_PRIVATE)
        registerNetworkStateListener()
        // start building okhttp client
        val clientBuilder = OkHttpClient.Builder()
        // setting connections timeout
        clientBuilder.readTimeout(60, TimeUnit.SECONDS)
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS)
        clientBuilder.addInterceptor(ConnectivityInterceptor(networkState))
        // add an interceptor to add authorization header and check the response
        clientBuilder.addInterceptor { chain ->
            // add authorization header to request
            val originalRequest: Request = chain.request()
            val newRequest: Request = originalRequest.newBuilder()
                    .addHeader("X-Authorization-Token", getAccessToken())
                    .method(originalRequest.method(), originalRequest.body())
                    .build()

            val body = newRequest.body()
            if (body != null) {
                val buffer = Buffer()
                body.writeTo(buffer)
                Log.d("REQUEST", buffer.readUtf8())
            }

            // get the response and check the response code
            val response = chain.proceed(newRequest)
            val mediaType = response.body()!!.contentType()
            val content = response.body()!!.string()
            Log.d("RESPONSE", content)

            try {
                val json = JSONObject(content)
                val data = json.get("Data")
                if (data is String && !json.getInt("Status").isSuccess()) json.put("Data", null)
                val responseWrapper = ResponseBody.create(mediaType, json.toString())
                return@addInterceptor response.newBuilder().body(responseWrapper).build()
            } catch (e: Exception) {
                return@addInterceptor response.newBuilder().body(ResponseBody.create(mediaType, content)).build()
            }
        }

        // set default date format for jackson object mapper
        val objectMapper = ObjectMapper()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        objectMapper.dateFormat = dateFormat as DateFormat
        objectMapper.registerModule(KotlinModule())

        // create the client
        val client: OkHttpClient = clientBuilder.build()
        // init retrofit object with created client
        retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build()
    }

    fun saveAccessToken(token: String) {
        preferences.edit().putString("access_token", token).apply()
    }

    fun getAccessToken(): String = preferences.getString("access_token", "no token yet")

    fun isLoggedIn(): Boolean = preferences.getBoolean("login_status", false)

    fun setIsLoggedIn(isLoggedIn: Boolean) {
        preferences.edit().putBoolean("login_status", isLoggedIn).apply()
    }

    fun saveUserCredentials(username: String, password: String) {
        preferences.edit().putString("username", username)
                .putString("password", AESCrypt.encrypt(password))
                .apply()
    }

    fun getPassword(): String = AESCrypt.decrypt(preferences.getString("password", ""))

    fun getUsername(): String = preferences.getString("username", "")

    inner class ConnectivityInterceptor(isNetworkActive: Observable<Boolean>) : Interceptor {

        private var isNetworkActive: Boolean = false

        init {
            isNetworkActive.subscribe(
                    { _isNetworkActive -> this.isNetworkActive = _isNetworkActive },
                    { _error -> Log.e("NetworkActive error ", _error.message) })
        }

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            return if (!isNetworkActive) {
                throw NoConnectivityException()
            } else {
                chain.proceed(chain.request())
            }
        }
    }

    inner class NoConnectivityException : IOException() {

        override val message: String
            get() = "No network available, please check your WiFi or Data connection"
    }

    private lateinit var receiver: BroadcastReceiver

    private fun registerNetworkStateListener() {
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (CONNECTIVITY_CHANGE_ACTION == action) {
                    //check internet connection
                    if (!ConnectionHelper.isConnected(context)) {
                        var show = false
                        if (ConnectionHelper.lastNoConnectionTs == -1L) {//first time
                            show = true
                            ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis()
                        } else {
                            if (System.currentTimeMillis() - ConnectionHelper.lastNoConnectionTs > 1000) {
                                show = true
                                ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis()
                            }
                        }

                        if (show && ConnectionHelper.isOnline) {
                            ConnectionHelper.isOnline = false
                            networkState.onNext(false)
                        }
                    } else {
                        // Perform your actions here
                        ConnectionHelper.isOnline = true
                        networkState.onNext(true)
                    }
                }
            }
        }
        registerReceiver(receiver, filter)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(receiver)
    }

    object ConnectionHelper {

        var lastNoConnectionTs: Long = -1L
        var isOnline = true

        fun isConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo

            return activeNetwork != null && activeNetwork.isConnected
        }
    }
}
