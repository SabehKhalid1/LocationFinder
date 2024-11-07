package com.example.location_finder

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocationDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "locations.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "location_table"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the location table
        val createTableStatement = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_LATITUDE REAL,
                $COLUMN_LONGITUDE REAL
            )
        """.trimIndent()
        db.execSQL(createTableStatement)

        // Optional: Prepopulate the database with sample data
        val locations = listOf(
            Triple("Oshawa", 43.8971, -78.8658),
            Triple("Ajax", 43.8509, -79.0204),
            Triple("Pickering", 43.8353, -79.0892),
            Triple("Scarborough", 43.7729, -79.2577),
            Triple("Downtown Toronto", 43.65107, -79.347015),
            Triple("Mississauga", 43.5890, -79.6441),
            Triple("Brampton", 43.7315, -79.7624),
            Triple("Markham", 43.8561, -79.3370)
            // Add more locations as needed to reach 100 entries
        )

        locations.forEach { (address, latitude, longitude) ->
            val values = ContentValues().apply {
                put(COLUMN_ADDRESS, address)
                put(COLUMN_LATITUDE, latitude)
                put(COLUMN_LONGITUDE, longitude)
            }
            db.insert(TABLE_NAME, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Function to add a location to the database
    fun addLocation(address: String, latitude: Double, longitude: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ADDRESS, address)
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
        }
        val result = db.insert(TABLE_NAME, null, values)
        return result != -1L
    }

    // Function to retrieve a location by address
    fun getLocationByAddress(address: String): Pair<Double, Double>? {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_LATITUDE, COLUMN_LONGITUDE), "$COLUMN_ADDRESS=?", arrayOf(address), null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE))
            val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
            cursor.close()
            Pair(latitude, longitude)
        } else {
            cursor?.close()
            null
        }
    }

    // Function to update a location by ID
    fun updateLocation(id: Int, address: String, latitude: Double, longitude: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ADDRESS, address)
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
        }
        val result = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        return result > 0
    }

    // Function to delete a location by ID
    fun deleteLocation(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        return result > 0
    }
}
