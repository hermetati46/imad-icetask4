package com.example.climo2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // App Name TextView
        val appNameTextView: TextView = findViewById(R.id.appNameTextView)
        appNameTextView.text = "Climo"

        // Student Info TextView
        val studentInfoTextView: TextView = findViewById(R.id.studentInfoTextView)
        studentInfoTextView.text = "Hermenegildo Tati - ST10494935"

        // Button to proceed to Main Screen
        val proceedButton: Button = findViewById(R.id.proceedButton)
        proceedButton.setOnClickListener {
            val intent = Intent(this, WeatherDetailsActivity::class.java)
            startActivity(intent)
        }
    }
}