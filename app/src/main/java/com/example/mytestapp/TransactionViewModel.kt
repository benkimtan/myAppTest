package com.example.mytestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing and exposing transaction data to the UI.
 */
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    // Expose the state of the transactions as a StateFlow
    val allTransactions: StateFlow<List<Transaction>> = repository.allTransactions
        .stateIn(
            scope = viewModelScope, 
            started = SharingStarted.WhileSubscribed(5000), 
            initialValue = emptyList()
        )

    /**
     * Adds a new transaction to the persistent storage.
     * @param transaction The transaction object to be saved.
     */
    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        // The Repository handles the persistence via the database
        repository.insertTransaction(transaction)
    }

    /**
     * Deletes a transaction based on its ID.
     * @param id The unique ID of the transaction to delete.
     */
    fun deleteTransaction(id: Long) = viewModelScope.launch {
        repository.deleteTransaction(id)
    }
}