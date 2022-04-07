package com.example.mobiiliprojektir9

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


//class OpenOrdersRepo {
//    private val firestore = FirebaseFirestore.getInstance()
//
//    @OptIn(ExperimantalCoroutineApi::class)
//    fun getOpenOrderDetails() = callbackFlow {
//        val collection = firestore.collection("Jobs")
//        val snapshotListener = collection.addSnapshotListener {value, error ->
//            val response = if (error == null){
//                OnSuccess(value)
//            } else{
//                OnError(error)
//            }
//            offer(response)
//        }
//        awaitClose {
//            snapshotListener.remove()
//        }
//    }
//}