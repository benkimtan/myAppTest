package com.example.mytestapp

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a financial transaction.
 * Converted to a Room Entity for persistent storage.
 */
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Added primary key
    val ticker: String,
    val quantity: Int
)