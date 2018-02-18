package com.hiloipa.app.hilo.utils

import com.hiloipa.app.hilo.models.requests.*
import com.hiloipa.app.hilo.models.responses.GoalTrackerResponse
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.SignUpResponse
import com.hiloipa.app.hilo.models.responses.UserData
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by eduardalbu on 31.01.2018.
 */
interface HiloAPI {

    @POST("Login")
    fun login(@Body loginRequest: LoginRequest): Observable<HiloResponse<UserData>>

    @POST("LogOut")
    fun logout(@Body logoutRequest: LogoutRequest): Observable<HiloResponse<Any>>

    @POST("GetSignup")
    fun getSignUpData(): Observable<HiloResponse<SignUpResponse>>

    @POST("Signup")
    fun createNewAccount(@Body registrationRequest: RegistrationRequest): Observable<HiloResponse<UserData>>

    @POST("ForgotPassword")
    fun resetPassword(@Body resetPassRequest: ResetPassRequest): Observable<HiloResponse<String>>

    @POST("GoalTracker")
    fun getGoalTrackerData(@Body goalTrackerRequest: GoalTrackerRequest = GoalTrackerRequest()):
            Observable<HiloResponse<GoalTrackerResponse>>
}