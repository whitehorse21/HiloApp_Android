package com.hiloipa.app.hilo.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by eduardalbu on 21.02.2018.
 */
class FutureTeamReachOuts(@JsonProperty("backlogs") val backlogs: ArrayList<TeamReachOutContact>?,
                          @JsonProperty("futurefollowups") val futureFollowups: ArrayList<TeamReachOutContact>)