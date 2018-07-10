package com.hilocrm.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 21.02.2018.
 */
class FutureFollowUps(@JsonProperty("backlogs") val backlogs: ArrayList<FollowUpContact>,
                      @JsonProperty("futurefollowups") val futureFollowups: ArrayList<FollowUpContact>)