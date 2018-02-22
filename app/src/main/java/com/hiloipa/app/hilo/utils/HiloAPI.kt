package com.hiloipa.app.hilo.utils

import com.hiloipa.app.hilo.models.requests.*
import com.hiloipa.app.hilo.models.responses.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

    @POST("ShowGoalPlans")
    fun showGoalPlans(@Body standardRequest: StandardRequest): Observable<HiloResponse<ShowPlansResponse>>

    @POST("DisplayAllGoalTracker")
    fun getFutureFollowUps(@Body standardRequest: StandardRequest): Observable<HiloResponse<FutureFollowUps>>

    @POST("DisplayAllGoalTracker")
    fun getFutureTeamReachOuts(@Body standardRequest: StandardRequest): Observable<HiloResponse<FutureTeamReachOuts>>

    @POST("DisplayAllGoalTracker")
    fun getFutureReachOutContacts(@Body standardRequest: StandardRequest): Observable<HiloResponse<ArrayList<ReachOutContact>>>

    @POST("ShowCompleteOption")
    fun showCompleteOption(@Body standardRequest: StandardRequest): Observable<HiloResponse<CompleteOption>>

    @POST("CompleteGoal")
    fun completeGoal(@Body completeRequest: CompleteReachRequest): Observable<HiloResponse<String>>

    @POST("RemoveGoalTrackerContact")
    fun removeGoalTrackerContact(@Body standardRequest: StandardRequest): Observable<HiloResponse<String>>

    @POST("GoalTrackerReachoutContacts")
    fun searchContacts(@Body standardRequest: StandardRequest): Observable<HiloResponse<ArrayList<SearchContact>>>

    @GET("GetTeamContact?")
    fun getTeamContactId(@Query("teamid") teamId: Int): Observable<ResponseBody>
}