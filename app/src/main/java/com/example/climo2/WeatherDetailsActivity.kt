package com.example.climo2

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class WeatherDetailsActivity : AppCompatActivity() {

    // Sample Hardcoded Data
    private val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    // Made maxTemps a var so it can be updated
    private var maxTemps = intArrayOf(25, 29, 22, 24, 20, 18, 16)
    // New array for weather conditions
    private val weatherConditions = arrayOf("Sunny", "Cloudy", "Rainy", "Sunny", "Windy", "Cloudy", "Partly Cloudy")

    private lateinit var weatherDataContainer: LinearLayout
    private lateinit var averageTempTextView: TextView

    // ActivityResultLauncher for getting results from EditTemperaturesActivity
    private val editTempsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedTemps = result.data?.getIntArrayExtra(EditTemperaturesActivity.EXTRA_UPDATED_TEMPS)
            if (updatedTemps != null && updatedTemps.size == maxTemps.size) {
                maxTemps = updatedTemps // Update the temperatures
                refreshWeatherData() // Refresh the display
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)

        weatherDataContainer = findViewById(R.id.weatherDataContainer)
        averageTempTextView = findViewById(R.id.averageTempTextView)
        val backButton: Button = findViewById(R.id.backButton)
        val editTempsButton: Button = findViewById(R.id.editTempsButton)

        refreshWeatherData() // Initial display of weather data

        // Handle Back button click
        backButton.setOnClickListener {
            finish()
        }

        // Handle Edit Temperatures button click
        editTempsButton.setOnClickListener {
            val intent = Intent(this, EditTemperaturesActivity::class.java)
            intent.putExtra(EditTemperaturesActivity.EXTRA_DAYS, days)
            intent.putExtra(EditTemperaturesActivity.EXTRA_CURRENT_TEMPS, maxTemps)
            editTempsLauncher.launch(intent) // Launch for result
        }
    }

    private fun refreshWeatherData() {
        // Clear previous data
        weatherDataContainer.removeAllViews()

        // Display each day with its max temperature and condition
        for (i in days.indices) {
            val dayTextView = TextView(this)
            // Display condition along with temp
            dayTextView.text = "${days[i]}: ${maxTemps[i]}°C - ${weatherConditions.getOrElse(i) { "N/A" }}"
            dayTextView.textSize = 22f
            dayTextView.setPadding(8, 8, 8, 8)
            dayTextView.setTextColor(Color.WHITE) // Set text color to white
            weatherDataContainer.addView(dayTextView)
        }

        // Calculate and show the average max temperature
        var sumOfTemps = 0
        for (temp in maxTemps) {
            sumOfTemps += temp
        }

        val averageTemp = if (maxTemps.isNotEmpty()) {
            sumOfTemps.toDouble() / maxTemps.size
        } else {
            0.0
        }

        val decimalFormat = DecimalFormat("#.##")
        averageTempTextView.text = "Average Max Temp: ${decimalFormat.format(averageTemp)}°C"
        averageTempTextView.textSize = 22f
        averageTempTextView.setTextColor(Color.WHITE)
    }
}