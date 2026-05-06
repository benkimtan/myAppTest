package com.example.mytestapp

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    // Original Views
    private lateinit var helloMessageView: TextView
    private lateinit var removeButton: Button
    // New Feature Views
    private lateinit var addTransactionButton: Button
    private lateinit var transactionRecyclerView: RecyclerView

    // State Management
    private val transactions = mutableListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the traditional XML layout
        setContentView(R.layout.activity_main)

        // 1. Initialize Views
        helloMessageView = findViewById(R.id.hello_message_view)
        removeButton = findViewById(R.id.remove_button)
        val helloButton: Button = findViewById(R.id.hello_button)
        addTransactionButton = findViewById(R.id.add_transaction_button) // New View
        transactionRecyclerView = findViewById(R.id.transaction_recycler_view) // New View

        // 2. Setup Transaction RecyclerView
        setupRecyclerView()

        // 3. Set up listeners
        setupListeners(helloButton)
        setupAddButtonListener() // New Listener
    }

    private fun setupRecyclerView() {
        // Initialize adapter and set it to the RecyclerView
        transactionAdapter = TransactionAdapter(transactions) { transaction, position ->
            // Callback when a delete button is clicked
            showDeleteConfirmation(transaction, position)
        }
        transactionRecyclerView.adapter = transactionAdapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupAddButtonListener() {
        addTransactionButton.setOnClickListener {
            showAddTransactionDialog()
        }
    }

    private fun showAddTransactionDialog() {
        // 1. Create the container
        val dialogView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10) // Improved padding
        }

        // 2. Create only the inputs (remove the bottomLayout and manual buttons)
        val tickerInput = EditText(this).apply {
            hint = "Stock Ticker (e.g., AAPL)"
            inputType = android.text.InputType.TYPE_CLASS_TEXT
        }
        val quantityInput = EditText(this).apply {
            hint = "Quantity (Integer)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        dialogView.addView(tickerInput)
        dialogView.addView(quantityInput)

        // 3. Use the Builder's built-in button logic
        AlertDialog.Builder(this)
            .setTitle("Add New Transaction")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ -> // "OK" text goes here
                val ticker = tickerInput.text.toString().trim()
                val quantityString = quantityInput.text.toString().trim()

                if (ticker.isNotEmpty() && quantityString.isNotEmpty()) {
                    val quantity = quantityString.toIntOrNull() ?: 0
                    if (quantity > 0) {
                        val newTransaction = Transaction(ticker, quantity)
                        transactions.add(newTransaction)
                        transactionAdapter.notifyItemInserted(transactions.size - 1)
                    } else {
                        Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun showDeleteConfirmation(transaction: Transaction, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete the transaction (${transaction.ticker}, ${transaction.quantity})?")
            .setPositiveButton("Yes") { dialog, which ->
                // Action: Remove item and update UI
                transactions.removeAt(position)
                transactionAdapter.notifyItemRemoved(position)
                Toast.makeText(this, "Transaction deleted.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, which ->
                // Action: Dismiss dialog with no changes
                dialog.cancel()
            }
            .show()
    }

    private fun setupListeners(helloButton: Button) {
        // Handle the 'Hello World' button (green) click (Original functionality)
        helloButton.setOnClickListener {
            if (helloMessageView.visibility != View.VISIBLE) {
                helloMessageView.visibility = View.VISIBLE
                helloMessageView.text = "Hello World Message Created!"
            }
            Toast.makeText(this, "Hello button pressed. Message is displayed/acknowledged.", Toast.LENGTH_SHORT).show()
        }

        // Handle the 'Remove Message' button (blue) click (Original functionality)
        removeButton.setOnClickListener {
            // Check if the "Hello World" message view is currently visible
            if (helloMessageView.visibility == View.VISIBLE) {
                // Remove the message: hide the view
                helloMessageView.visibility = View.GONE
                Toast.makeText(this, "Hello World message successfully removed.", Toast.LENGTH_SHORT).show()
            } else {
                // Do nothing and notify user if no message is present
                Toast.makeText(this, "No 'Hello World' message found to remove. Nothing to do.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}