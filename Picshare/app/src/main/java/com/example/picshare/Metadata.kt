package com.example.picshare

import com.android.volley.RequestQueue
import com.example.picshare.domain.User

object Metadata {
    lateinit var thisUser: User
    const val serverURL = "7296-86-57-130-172.ngrok.io"
    lateinit var requests : RequestQueue
}