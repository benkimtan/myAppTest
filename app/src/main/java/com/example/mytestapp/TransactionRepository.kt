package com.example.mytestapp

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class to mediate between the data sources (DAO) and the ViewModel.
 */
@Singleton
class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {
    
    // Expose the flow of all transactions to the ViewModel
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    /**
     * Inserts a new transaction into the database.
     */
    suspend fun insertTransaction(transaction: Transaction) {
        // We use the provided transaction data; the DAO handles the auto-generated ID.
        transactionDao.insert(transaction)
    }

    /**
     * Deletes a transaction by its unique ID.
     * @param transactionId The ID of the transaction to delete.
     */
    suspend fun deleteTransaction(transactionId: Long) {
        // Fetch the transaction first to use the @Delete method, or use the specific deleteById method.
        // Since we rely on ID, let's update the DAO/Repository pattern slightly for direct deletion by ID.
        // We will use the deleteById method defined in the DAO for efficiency.
        transactionDao.deleteById(transactionId)
    }
}