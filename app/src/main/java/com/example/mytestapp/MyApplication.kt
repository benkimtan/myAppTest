package com.example.mytestapp

import android.app.Application

class MyApplication : Application() {
    // Instantiate the database and repository only when needed
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { TransactionRepository(database.transactionDao()) }
}