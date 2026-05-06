package com.example.mytestapp

/**
 * Data class representing a financial transaction.
 * @param ticker The stock ticker symbol (e.g., AAPL).
 * @param quantity The number of shares bought/sold.
 */
data class Transaction(
    val ticker: String,
    val quantity: Int
)