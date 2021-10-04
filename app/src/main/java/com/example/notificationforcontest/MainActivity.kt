package com.example.notificationforcontest

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notificationforcontest.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var timePicker: MaterialTimePicker
    private lateinit var cal: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        binding.setTimeButton.setOnClickListener {
            setTimeNow()
        }

        binding.setAlarmButton.setOnClickListener {
            setAlarmNow()
        }

        binding.cancelAlarmButton.setOnClickListener {
            cancelAlarmNow()
        }
    }

    private fun cancelAlarmNow() {

        val i = Intent(this,AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this,0,i,0)

        if(alarmManager == null){
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }

        alarmManager.cancel(pendingIntent)
        Toast.makeText(this,"Alarm cancelled successfully",Toast.LENGTH_LONG).show()
        startActivity(Intent(this,MainActivity::class.java))
    }

    private fun setAlarmNow() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val i = Intent(this,AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this,0,i,0)

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,cal.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)

        Toast.makeText(this,"Alarm set successfully for ${binding.showTime.text}",Toast.LENGTH_LONG).show()

    }

    private fun setTimeNow() {
        timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        timePicker.show(supportFragmentManager,"Samkit5025")

        timePicker.addOnPositiveButtonClickListener {
            if(timePicker.hour > 12){

                val temp = String.format("%2d",(timePicker.hour-12)) + " : " + String.format("%2d",timePicker.minute) + " PM"
                binding.showTime.text = temp
            }
            else{
                val temp = String.format("%2d",timePicker.hour) + " : " + String.format("%2d",timePicker.minute) + " AM"
                binding.showTime.text = temp
            }


            cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY,timePicker.hour)
            cal.set(Calendar.MINUTE,timePicker.minute)
            cal.set(Calendar.SECOND,0)
            cal.set(Calendar.MILLISECOND,0)
        }
    }

    private fun createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Alarm Remainder Channel"
            val des = "Channel for contest remainder"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel("Samkit5025",name,importance).apply {
                description = des
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}