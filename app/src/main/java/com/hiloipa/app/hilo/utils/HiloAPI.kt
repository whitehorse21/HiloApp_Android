package com.hiloipa.app.hilo.utils

import com.hiloipa.app.hilo.models.responses.MessageScript
import com.hiloipa.app.hilo.models.requests.*
import com.hiloipa.app.hilo.models.responses.*
import com.hiloipa.app.hilo.ui.todos.ActionDropDown
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

    /**
     *     Authentication calls
     */

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

    /**
     *    Goal tracker calls
     */

    @POST("GoalTracker")
    fun getGoalTrackerData(@Body goalTrackerRequest: GoalTrackerRequest = GoalTrackerRequest()):
            Observable<HiloResponse<GoalTrackerResponse>>

    @POST("ShowGoalPlans")
    fun showGoalPlans(@Body standardRequest: StandardRequest): Observable<HiloResponse<GoalPlans>>

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
    fun removeGoalTrackerContact(@Body standardRequest: StandardRequest = StandardRequest()): Observable<HiloResponse<String>>

    @POST("GoalTrackerReachoutContacts")
    fun searchContacts(@Body standardRequest: StandardRequest = StandardRequest()): Observable<HiloResponse<ArrayList<SearchContact>>>

    @GET("GetTeamContact?")
    fun getTeamContactId(@Query("teamid") teamId: Int): Observable<ResponseBody>

    @POST("AddGoalTrackerContact")
    fun addGoalTrackerContact(@Body standardRequest: StandardRequest = StandardRequest()): Observable<HiloResponse<String>>

    @POST("SaveGoalPlans")
    fun saveGoalPlan(@Body savePlanRequest: SavePlanRequest): Observable<HiloResponse<String>>

    /**
     *    Contacts calls
     */

    @POST("ContactsList")
    fun getContactsList(@Body contactsListRequest: ContactsListRequest): Observable<HiloResponse<DetailedContacs>>

    @POST("SearchContactsList")
    fun searchDetailedContacts(@Body contactsListRequest: ContactsListRequest): Observable<HiloResponse<DetailedContacs>>

    @POST("ImportContacts")
    fun importContacts(@Body importContactsRequest: ImportContactsRequest): Observable<HiloResponse<ImportContactsResponse>>

    @POST("DeleteContact")
    fun deleteContact(@Body deleteContactRequest: DeleteContactRequest): Observable<HiloResponse<String>>

    @POST("ContactDetails")
    fun getContactFullDetails(@Body standardRequest: StandardRequest = StandardRequest()): Observable<HiloResponse<FullContactDetails>>

    @POST("FilterListValue")
    fun getFilterData(@Body standardRequest: StandardRequest = StandardRequest()): Observable<HiloResponse<FilterData>>

    @POST("FilterContacts")
    fun filterContacts(@Body filterRequest: FilterRequest): Observable<HiloResponse<DetailedContacs>>

    @POST("Addfollowup")
    fun addToGoalTracker(@Body addToTrackerRequest: AddToTrackerRequest): Observable<HiloResponse<String>>

    @POST("CampaignsDropDown")
    fun getCampaignData(@Body standardRequest: StandardRequest = StandardRequest()): Observable<HiloResponse<CampaignsData>>

    @POST("AssignCampaign")
    fun assignCampaign(@Body assignCampaignRequest: AssignCampaignRequest): Observable<HiloResponse<String>>

    @POST("GetScripts")
    fun getScripts(@Body smsScriptsRequest: GetSmsScriptsRequest): Observable<HiloResponse<ArrayList<MessageScript>>>

    @POST("GetAddContact")
    fun getDataForNewContact(@Body standardRequest: StandardRequest = StandardRequest()): Observable<HiloResponse<NewContactData>>

    @POST("UpdateContact")
    fun updateContactDetails(@Body saveContactRequest: SaveContactRequest): Observable<HiloResponse<String>>

    @POST("AddContact")
    fun addNewContact(@Body saveContactRequest: SaveContactRequest): Observable<HiloResponse<String>>

    @POST("BulkUpdate")
    fun getBulkUpdateValues(@Body standardRequest: StandardRequest = StandardRequest()): Observable<HiloResponse<BulkUpdate>>

    @POST("BulkUpdateContact")
    fun bulkUpdateContact(@Body bulkUpdateRequest: BulkUpdateRequest): Observable<HiloResponse<String>>

    /**
     *    Reach out logs
     */

    @POST("ReachoutLog")
    fun getReachOutLogs(@Body reachOutLogsRequest: ReachOutLogsRequest): Observable<HiloResponse<ReachOutLogs>>

    @POST("DeleteReachoutLog")
    fun deleteReachOutLog(@Body deleteReachOutRequest: DeleteReachOutRequest): Observable<HiloResponse<String>>

    @POST("AddContactReachoutLog")
    fun saveReachOutLog(@Body saveReachOutLogRequest: SaveReachOutLogRequest): Observable<HiloResponse<String>>

    /**
     *    To Dos
     */

    @POST("DashBoard")
    fun getToDoDashboard(@Body standardRequest: StandardRequest): Observable<HiloResponse<ToDoData>>

    @POST("CommonCompleted")
    fun completeGoal(@Body completeGoalRequest: CompleteGoalRequest): Observable<ResponseBody>

    @POST("ContactActions")
    fun getContactActions(@Body standardRequest: StandardRequest): Observable<HiloResponse<ActionDropDown>>

    @POST("AddActionForGoal")
    fun addActionForGoal(@Body actionForGoalRequest: ActionForGoalRequest): Observable<HiloResponse<Int>>

    @POST("AddGoal")
    fun addGoal(@Body actionForGoalRequest: ActionForGoalRequest): Observable<HiloResponse<String>>

    @POST("UpdateGoal")
    fun updateGoal(@Body actionForGoalRequest: ActionForGoalRequest): Observable<HiloResponse<String>>

    @POST("Addaction")
    fun addAction(@Body actionForGoalRequest: ActionForGoalRequest): Observable<HiloResponse<String>>

    @POST("AddTeamneed")
    fun addTeamNead(@Body actionForGoalRequest: ActionForGoalRequest): Observable<HiloResponse<String>>

    @POST("AddActionToGoal")
    fun addActionToGoal(@Body actionToGoalRequest: ActionToGoalRequest): Observable<HiloResponse<String>>

    @POST("Addnewevents")
    fun saveEvent(@Body saveEventRequest: SaveEventRequest): Observable<HiloResponse<String>>
}