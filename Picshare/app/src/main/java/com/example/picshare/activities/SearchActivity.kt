package com.example.picshare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.picshare.Metadata.thisUser
import com.example.picshare.R
import com.example.picshare.domain.User
import com.example.picshare.service.SubscriberService
import com.example.picshare.service.UserService
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.ExecutionException

class SearchActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var btFind: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setTitle(R.string.subscribe_title)
        bindLateinits()
        setListeners()
    }

    private fun bindLateinits() {
        etUsername = findViewById(R.id.etUsername)
        btFind = findViewById(R.id.btFind)
    }

    private fun setListeners() {
        btFind.setOnClickListener { v -> onFindClick(v) }
    }

    private fun onFindClick(v: View?) {
        startFindUser(v)
    }

    private fun startFindUser(v: View?) {
        val username = etUsername.text.toString()
        val futureUser = UserService.findByUserName(username)
        val t = Thread {
            try {
                val user: User = futureUser.get()
                startCheckSubscriber(v, user.id)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
                Snackbar.make(v!!, getString(R.string.user_not_found_msg), LENGTH_SHORT).show()
            }
        }
        t.start()
    }

    private fun startCheckSubscriber(v : View?, userId: Int) {
        val futureUsers = SubscriberService.getSubscribers(userId.toString())
        val t = Thread {
            try {
                val users = futureUsers.get()
                for (user in users) {
                    if (user.id == thisUser.id){
                        Snackbar.make(v!!, getString(R.string.already_subscribed_msg), LENGTH_SHORT).show()
                        return@Thread
                    }
                }
                SubscriberService.subscribe(thisUser.id, userId)
                Snackbar.make(v!!, R.string.successfully_subscribed_msg, LENGTH_SHORT).show()
                finish()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
                Snackbar.make(v!!, getString(R.string.user_not_found_msg), LENGTH_SHORT).show()
            }
        }
        t.start()
    }
}