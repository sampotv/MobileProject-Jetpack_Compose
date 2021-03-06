package com.example.mobiiliprojektir9

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.google.type.DateTime
import java.util.*

data class Order(
    var order_id: String = "",
    var address_from: String = "",
    var address_to: String = "",
    var content: String = "",
    var company: String = "",
    var driver_id: String = "",
    var state: String = "",
    var imageUrl: String = "",
//    @ServerTimestamp
    var time_created: Timestamp? = null,
//    @ServerTimestamp
    var time_delivered: Timestamp? = null
)