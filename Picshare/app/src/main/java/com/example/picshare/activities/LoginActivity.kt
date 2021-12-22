package com.example.picshare.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.set
import com.android.volley.toolbox.Volley
import com.example.picshare.Metadata
import com.example.picshare.R
import com.example.picshare.domain.User
import com.example.picshare.service.SubscriberService
import com.example.picshare.service.UserService
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.ExecutionException

class LoginActivity : AppCompatActivity() {
    private var uname: EditText? = null
    var loginBtn: Button? = null
    var pref: SharedPreferences? = null
    var inten: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Metadata.requests = Volley.newRequestQueue(this)
        uname = findViewById(R.id.txtName)
        loginBtn = findViewById(R.id.btnLogin)
        pref = getSharedPreferences("user_details", MODE_PRIVATE)
        inten = Intent(this@LoginActivity, DrawActivity::class.java)
        if (pref!!.contains("username") && pref!!.contains("id")) {
            startActivity(inten)
            Metadata.thisUser = User(pref!!.getInt("id", 0), pref!!.getString("username", "")!!,
                "")
        }
        loginBtn!!.setOnClickListener {
            val username: String = uname!!.text.toString()
            startAddUser(it, username)
        }
    }

    private fun startAddUser(v: View?, username: String) {
        val futureUsers = UserService.addUser(username)
        val t = Thread {
            try {
                val jsonUser = futureUsers.get()
                Metadata.thisUser = User(jsonUser.getInt("id"),
                    jsonUser.getString("username"), jsonUser.getString("password"))
                Log.d("USER", Metadata.thisUser.toString())
                val editor: SharedPreferences.Editor = pref!!.edit()
                editor.putInt("id", Metadata.thisUser.id)
                editor.putString("username", username)
                editor.apply()
                startActivity(inten)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
                Snackbar.make(
                    v!!, "The name is already taken!",
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
                uname!!.text.clear()
            }
        }
        t.start()
    }
}