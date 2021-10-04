package com.example.notificationforcontest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        findViewById<TextView>(R.id.textView).setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}