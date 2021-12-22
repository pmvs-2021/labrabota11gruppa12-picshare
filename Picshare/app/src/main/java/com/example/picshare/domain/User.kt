package com.example.picshare.domain

class User(var id: Int, var username: String, var password: String) {
    override fun toString(): String {
        return "User(id=$id, username='$username', password='$password')"
    }
}