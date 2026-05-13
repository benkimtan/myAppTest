package com.example.mytestapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.CoroutineScope

/**
 * Adapter for the RecyclerView to display transaction rows.
 */
class TransactionAdapter(
    private val scope: CoroutineScope,
    private val viewModel: TransactionViewModel,
    private val onDeleteClicked: (Transaction, Int) -> Unit // New callback dependency
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {
//    private var transactions: List<Transaction> = emptyList()

    // --- Private methods (Keep ViewHolder and etc. logic) ---
// Note: ViewHolder definition remains unchanged.
// The binding logic will now use the transaction ID for deletion.
// -------------------------------------------------------------

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
        val transaction = getItem(position)
        holder.detailsText.text = "${transaction.ticker} - ${transaction.quantity}"

        // Set up the delete button listener
        holder.deleteButton.setOnClickListener {
            // Pass the transaction object and its adapter position to the delete listener callback
            onDeleteClicked(transaction, position)
        }
    }
}

class TransactionDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id // Checks if it's the same row
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem // Checks if the data inside the row changed
    }
}