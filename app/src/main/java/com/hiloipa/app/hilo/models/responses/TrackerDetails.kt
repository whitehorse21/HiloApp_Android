package com.hiloipa.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 21.02.2018.
 */
class TrackerDetails<T: Contact>(@JsonProperty("backlogs") val backLogs: ArrayList<T>,
                                 @JsonProperty("futurefollowups") val futureFollowups: ArrayList<T>)