package com.example.picshare

import com.android.volley.RequestQueue
import com.example.picshare.domain.User

object Metadata {
    lateinit var thisUser: User
    val serverURL = "b839-217-21-43-95.ngrok.io"
    lateinit var requests : RequestQueue
}