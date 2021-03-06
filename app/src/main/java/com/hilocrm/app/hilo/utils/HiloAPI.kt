package com.hilocrm.app.hilo.utils

import com.hilocrm.app.hilo.models.responses.MessageScript
import com.hilocrm.app.hilo.models.requests.*
import com.hilocrm.app.hilo.models.responses.*
import com.hilocrm.app.hilo.ui.todos.ActionDropDown
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

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

    @POST("{searchUrl}")
    fun searchContacts(@Path("searchUrl") searhUrl: String,
                       @Body standardRequest: GoalTrackerContacts = GoalTrackerContacts()): Observable<HiloResponse<ArrayList<SearchContact>>>

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

    /**
     *    More
     */

    @POST("updateProfileImage")
    fun updateUserAvatar(@Body standardRequest: StandardRequest): Observable<HiloResponse<String>>

    @POST("DisplayAllNotePads")
    fun getNotepadNotes(@Body standardRequest: StandardRequest): Observable<HiloResponse<NotepadNotes>>

    @POST("DeleteUserNote")
    fun deleteNote(@Body deleteNoteRequest: DeleteNoteRequest): Observable<HiloResponse<String>>

    @POST("RemoveTagFromNote")
    fun deleteTagFromNote(@Body deleteNoteRequest: DeleteNoteRequest): Observable<HiloResponse<String>>

    @POST("Addusernote")
    fun saveUserNote(@Body saveNoteRequest: SaveNoteRequest): Observable<HiloResponse<String>>

    @POST("NotepadTagsList")
    fun getTagsList(@Body standardRequest: StandardRequest): Observable<HiloResponse<NoteTagsResponse>>

    @POST("RemoveTag")
    fun deleteTag(@Body request: DeleteTagRequest): Observable<HiloResponse<String>>

    @POST("AddNoteTag")
    fun addNoteTag(@Body deleteTagRequest: DeleteTagRequest): Observable<HiloResponse<String>>

    @POST("Feedback")
    fun sendFeedback(@Body standardRequest: StandardRequest): Observable<HiloResponse<String>>

    @POST("DisplayAllProducts")
    fun getAllProducts(@Body standardRequest: StandardRequest): Observable<HiloResponse<AllProductsResponse>>

    @POST("RevealProductsContactsList")
    fun getProductContacts(@Body standardRequest: StandardRequest): Observable<HiloResponse<RevealProductsContacts>>

    @POST("AssigningProduct")
    fun assignProduct(@Body assignProductRequest: AssignProductRequest): Observable<HiloResponse<String>>

    @POST("GetScripts")
    fun getScripts(@Body scriptsRequest: ScriptsRequest): Observable<HiloResponse<ArrayList<Script>>>

    @POST("DeleteScript")
    fun deleteScript(@Body deleteScriptRequest: DeleteScriptRequest): Observable<HiloResponse<String>>

    @POST("AddScript")
    fun addScript(@Body saveScriptRequest: SaveScriptRequest): Observable<HiloResponse<String>>

    @POST("EditScript")
    fun updateScript(@Body saveScriptRequest: SaveScriptRequest): Observable<HiloResponse<String>>

    @POST("GetTemplates")
    fun getTemplates(@Body standardRequest: StandardRequest): Observable<HiloResponse<ArrayList<Template>>>

    @POST("GetAccount")
    fun getAccount(@Body standardRequest: StandardRequest = StandardRequest()): Observable<HiloResponse<Account>>

    @POST("UpdateAccount")
    fun updateAccount(@Body request: UpdateAccountRequest): Observable<HiloResponse<String>>

    @POST("InAppPurchase")
    fun changePaymentPlan(@Body request: ChangePaymentRequest): Observable<HiloResponse<String>>

    /**
     *    Contact Details
     */

    @POST("updateContactImage")
    fun updateContanctImage(@Body standardRequest: StandardRequest): Observable<HiloResponse<String>>

    @POST("DisplayContactnote")
    fun getContactNotes(@Body standardRequest: StandardRequest): Observable<HiloResponse<DisplayContactNotes>>

    @POST("DeleteContactNote")
    fun deleteContactNote(@Body request: DeleteContactNote): Observable<HiloResponse<String>>

    @POST("AddContactnote")
    fun saveContactNote(@Body request: SaveContactNote): Observable<HiloResponse<String>>

    @POST("ProductsListCategory")
    fun getContactProducts(@Body request: StandardRequest): Observable<HiloResponse<ContactProducts>>

    @POST("UnAssignProducts")
    fun unassignProduct(@Body request: UnassignProduct): Observable<HiloResponse<String>>

    @POST("ContactDocumentsList")
    fun getDocuments(@Body request: StandardRequest): Observable<HiloResponse<DocumentResponse>>

    @POST("DisplayContactCampaigns")
    fun getCampaigns(@Body request: StandardRequest): Observable<HiloResponse<CampaignsResponse>>
}