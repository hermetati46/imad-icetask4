package com.example.climo2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
// import android.widget.ScrollView // ScrollView removed
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat // For getting drawable
import androidx.core.view.setPadding // For setting padding in dp if desired, though current is pixels

class EditTemperaturesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DAYS = "com.example.climo2.EXTRA_DAYS"
        const val EXTRA_CURRENT_TEMPS = "com.example.climo2.EXTRA_CURRENT_TEMPS"
        const val EXTRA_UPDATED_TEMPS = "com.example.climo2.EXTRA_UPDATED_TEMPS"
    }

    private lateinit var days: Array<String>
    private lateinit var currentTemps: IntArray
    private val editTextList = mutableListOf<EditText>() // To hold references to EditTexts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve data from Intent
        days = intent.getStringArrayExtra(EXTRA_DAYS) ?: arrayOf()
        currentTemps = intent.getIntArrayExtra(EXTRA_CURRENT_TEMPS) ?: intArrayOf()

        if (days.isEmpty() || currentTemps.isEmpty() || days.size != currentTemps.size) {
            Toast.makeText(this, "Error: Invalid data received.", Toast.LENGTH_LONG).show()
            finish() // Close activity if data is invalid
            return
        }

        // Create layout programmatically
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            // For real app, convert 16 to dp:
            // val paddingDp = (16 * resources.displayMetrics.density).toInt()
            // setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
            setPadding(16) // Current: uses pixels.
            // Set background to gradient_bg drawable
            background = ContextCompat.getDrawable(this@EditTemperaturesActivity, R.drawable.gradient_bg)
        }

        val titleTextView = TextView(this).apply {
            text = "Edit Daily Temperatures"
            textSize = 22f
            gravity = Gravity.CENTER_HORIZONTAL
            setTextColor(android.graphics.Color.WHITE)
            // For real app, convert 16 to dp for bottom padding
            setPadding(0,0,0,16) // bottom padding in pixels
        }
        mainLayout.addView(titleTextView)

        // Container for input fields (no longer wrapped in ScrollView)
        val inputContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        // Dynamically create EditText for each day
        for (i in days.indices) {
            val dayLabel = TextView(this).apply {
                text = days[i]
                textSize = 18f
                setTextColor(android.graphics.Color.WHITE)
            }
            inputContainer.addView(dayLabel)

            val tempEditText = EditText(this).apply {
                setText(currentTemps[i].toString())
                inputType = android.text.InputType.TYPE_CLASS_NUMBER
                textSize = 18f
                setTextColor(android.graphics.Color.WHITE)
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                // For real app, convert 8 to dp for bottom margin
                lp.bottomMargin = 8 // bottom margin in pixels
                layoutParams = lp
            }
            editTextList.add(tempEditText)
            inputContainer.addView(tempEditText)
        }
        // Add inputContainer directly to mainLayout
        // Decide on LayoutParams: wrap_content or match_parent with weight if needed
        // For now, let's use MATCH_PARENT for width and WRAP_CONTENT for height
        val inputContainerParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT // Or 0 with weight 1.0f if it should expand
        )
        mainLayout.addView(inputContainer, inputContainerParams)


        val saveButton = Button(this).apply {
            text = "Save Temperatures"
            setOnClickListener {
                saveTemperatures()
            }
        }
        val buttonParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            // For real app, convert 16 to dp for top margin
            topMargin = 16 // top margin in pixels
        }
        mainLayout.addView(saveButton, buttonParams)

        setContentView(mainLayout)
    }

    private fun saveTemperatures() {
        val updatedTemps = IntArray(days.size)
        var allValid = true

        for (i in editTextList.indices) {
            try {
                updatedTemps[i] = editTextList[i].text.toString().toInt()
            } catch (e: NumberFormatException) {
                editTextList[i].error = "Invalid number"
                // Show a Toast message for the first error encountered
                if (allValid) { // only show toast for the first error
                    Toast.makeText(this, "Please enter valid numbers for all temperatures.", Toast.LENGTH_SHORT).show()
                }
                allValid = false
            }
        }

        if (allValid) {
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_UPDATED_TEMPS, updatedTemps)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            // Toast already shown for the first error, or you can show a general one here if preferred
            // Toast.makeText(this, "Please correct the errors.", Toast.LENGTH_SHORT).show()
        }
    }
}