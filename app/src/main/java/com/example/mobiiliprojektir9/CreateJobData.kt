package com.example.mobiiliprojektir9

import com.google.firebase.Timestamp
import java.sql.Time

data class CreateJobData(
    var address_from : String = "",
    var address_to : String = "",
    var company : String = "",
    var content : String = "",
    var driver_id : String = "",
    var state : String = "",
    var time_created : java.sql.Timestamp = java.sql.Timestamp(1,1,1,1,1,1,1)
)
