package com.example.mytestapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

/**
 * Adapter for the RecyclerView to display transaction rows.
 */
class TransactionAdapter(
    private val transactions: MutableList<Transaction>,
    private val deleteListener: (Transaction, Int) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    // ViewHolder class to hold the views for a single transaction row
    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detailsText: TextView = itemView.findViewById(R.id.transaction_details_text)
        val deleteButton: Button = itemView.findViewById(R.id.delete_transaction_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_row, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.detailsText.text = "${transaction.ticker} - ${transaction.quantity}"

        // Set up the delete button listener
        holder.deleteButton.setOnClickListener {
            // Pass the transaction object and its adapter position to the delete listener callback
            deleteListener(transaction, position)
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}