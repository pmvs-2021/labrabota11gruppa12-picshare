package com.example.picshare

import com.android.volley.RequestQueue
import com.example.picshare.domain.User

object Metadata {
    lateinit var thisUser: User
    const val serverURL = "cadc-37-214-44-90.ngrok.io"
    lateinit var requests : RequestQueue
}