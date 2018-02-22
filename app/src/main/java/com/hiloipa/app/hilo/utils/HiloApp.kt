package com.hiloipa.app.hilo.utils

import android.content.Context
import android.content.SharedPreferences
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.hiloipa.app.hilo.BuildConfig
import com.hiloipa.app.hilo.models.responses.UserData
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by eduardalbu on 31.01.2018.
 */
class HiloApp: MultiDexApplication() {

    private lateinit var preferences: SharedPreferences

    companion object {
        lateinit var instance: HiloApp
        lateinit var retrofit: Retrofit
        lateinit var userData: UserData
        fun api(): HiloAPI = retrofit.create(HiloAPI::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        preferences = getSharedPreferences("hilo_app_prefs", Context.MODE_PRIVATE)
        // start building okhttp client
        val clientBuilder = OkHttpClient.Builder()
        // setting connections timeout
        clientBuilder.readTimeout(60, TimeUnit.SECONDS)
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS)
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
                if (data is String && data.isEmpty()) json.put("Data", null)
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
}
