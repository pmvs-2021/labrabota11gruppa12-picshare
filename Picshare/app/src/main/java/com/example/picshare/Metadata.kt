package com.example.picshare

import com.android.volley.RequestQueue
import com.example.picshare.domain.User

object Metadata {
    lateinit var thisUser: User
    const val serverURL = "024f-46-56-247-30.ngrok.io"
    lateinit var requests : RequestQueue
}