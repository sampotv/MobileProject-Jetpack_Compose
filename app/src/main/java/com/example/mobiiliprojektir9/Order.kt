package com.example.mobiiliprojektir9

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.google.type.DateTime
import java.util.*

data class Order(
    var address_from: String = "",
    var address_to: String = "",
    var content: String = "",
    var company: String = "",
    var driver_id: String = "",
    var state: String = "",
    @ServerTimestamp
    var time_created: Timestamp? = null,
    @ServerTimestamp
    var time_delivered: Timestamp? = null
)