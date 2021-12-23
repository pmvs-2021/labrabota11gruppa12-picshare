package com.example.picshare.domain

import java.util.*

class Message {
    var image: ByteArray? = null
    var sender: User? = null
    var receiver: User? = null
    var time: Calendar? = null
}