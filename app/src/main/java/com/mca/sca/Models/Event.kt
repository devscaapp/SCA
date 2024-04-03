package com.mca.sca.Models

import com.google.firebase.firestore.DocumentId

data class Event(
    val imageUrl: String? = null,
    val eventName: String?= null,
    val details: String?=null,
    val action: String?=null,
    val eventType: String?=null,
    val sr_no: String?=null
)
