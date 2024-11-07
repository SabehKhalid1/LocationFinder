package com.example.location_finder

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: LocationDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Sets the XML layout for this activity

        // Initialize the database helper
        dbHelper = LocationDatabaseHelper(this)

        // Reference UI elements from activity_main.xml
        val editTextAddress = findViewById<EditText>(R.id.editTextAddress)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)
        val buttonSearch = findViewById<Button>(R.id.buttonSearch)

        // Example default coordinates
        val defaultLatitude = 43.65107
        val defaultLongitude = -79.347015

        // Add Location functionality
        buttonAdd.setOnClickListener {
            val address = editTextAddress.text.toString()
            if (address.isNotBlank()) {
                val success = dbHelper.addLocation(address, defaultLatitude, defaultLongitude)
                textViewResult.text = if (success) "Location added successfully" else "Error adding location"
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            }
        }

        // Search Location functionality
        buttonSearch.setOnClickListener {
            val address = editTextAddress.text.toString()
            if (address.isNotBlank()) {
                val location = dbHelper.getLocationByAddress(address)
                if (location != null) {
                    textViewResult.text = "Latitude: ${location.first}, Longitude: ${location.second}"
                } else {
                    textViewResult.text = "Location not found"
                }
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            }
        }

        // Update Location functionality
        buttonUpdate.setOnClickListener {
            val address = editTextAddress.text.toString()
            val id = 1 // Replace with dynamic ID as needed
            val newLatitude = 43.7000
            val newLongitude = -79.4000
            if (address.isNotBlank()) {
                val success = dbHelper.updateLocation(id, address, newLatitude, newLongitude)
                textViewResult.text = if (success) "Location updated successfully" else "Error updating location"
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete Location functionality
        buttonDelete.setOnClickListener {
            val id = 1 // Replace with dynamic ID as needed
            val success = dbHelper.deleteLocation(id)
            textViewResult.text = if (success) "Location deleted successfully" else "Error deleting location"
        }
    }
}
