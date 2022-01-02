package com.example.picshare

import com.android.volley.RequestQueue
import com.example.picshare.domain.User

object Metadata {
    lateinit var thisUser: User
    const val serverURL = "7dcd-46-56-202-59.ngrok.io"
    lateinit var requests : RequestQueue
}