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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.ArrayAdapter

class MainActivity : AppCompatActivity() {
    // Original Views
    private lateinit var helloMessageView: TextView
    private lateinit var removeButton: Button
    // New Feature Views
    private lateinit var addTransactionButton: Button
    private lateinit var transactionRecyclerView: RecyclerView
    private lateinit var viewSelectorSpinner: Spinner
    private lateinit var originalContentContainer: LinearLayout

    // Dependencies (Using simple implementation for now, assumes Hilt setup later)
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the traditional XML layout
        setContentView(R.layout.activity_main)

        // 1. Initialize Views
        helloMessageView = findViewById(R.id.hello_message_view)
        removeButton = findViewById(R.id.remove_button)
        val helloButton: Button = findViewById(R.id.hello_button)
        addTransactionButton = findViewById(R.id.add_transaction_button)
        transactionRecyclerView = findViewById(R.id.transaction_recycler_view)
        viewSelectorSpinner = findViewById(R.id.view_selector_spinner)
        originalContentContainer = findViewById(R.id.original_content_container)

        // 2. Initialize ViewModel (Simplified manual injection for this scope)
        // NOTE: In a real app, Hilt/DI should provide this.
        val repository = TransactionRepository(
            (application as MyApplication).database.transactionDao()
        )
        transactionViewModel = TransactionViewModel(repository)

        // 3. Setup Transaction RecyclerView and Flow observation
        setupRecyclerView()
        setupTransactionFlowObservation()

        // 4. Set up listeners
        setupListeners(helloButton)
        setupAddButtonListener()

        // Configure the view selector spinner
        val spinnerItems = listOf("Hello World", "Ticker Tracker")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewSelectorSpinner.adapter = spinnerAdapter
        viewSelectorSpinner.setSelection(0) // default to "Hello World"

viewSelectorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                // Hello World view
                originalContentContainer.visibility = View.VISIBLE
                transactionRecyclerView.visibility = View.GONE
                addTransactionButton.visibility = View.GONE
            }
            1 -> {
                // Ticker Tracker view
                originalContentContainer.visibility = View.GONE
                transactionRecyclerView.visibility = View.VISIBLE
                addTransactionButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // No action needed
    }
}
    }

    private fun setupTransactionFlowObservation() {
        // Collect transactions from the ViewModel flow and update the adapter whenever data changes
        lifecycleScope.launch {
            transactionViewModel.allTransactions.collectLatest { transactions ->
                // Update the adapter's dataset and notify the change
                transactionAdapter.submitList(transactions)
            }
        }
    }

    private fun setupRecyclerView() {
        // Initialize adapter, passing the delete confirmation callback.
        transactionAdapter = TransactionAdapter(
            lifecycleScope,
            transactionViewModel,
            this::showDeleteConfirmation
        )
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
            setPadding(50, 40, 50, 10)
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
                        // Create transaction, ensuring we pass a dummy ID since the DB handles it
                        val newTransaction = Transaction(ticker= ticker, quantity = quantity)

                        // 💡 CORE CHANGE: Use ViewModel to persist data
                        lifecycleScope.launch {
                            transactionViewModel.addTransaction(newTransaction)
                        }
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
            .setMessage("Are you sure you want to delete the transaction (${transaction.ticker}, ${transaction.quantity})? This action is permanent.")
            .setPositiveButton("Yes") { dialog, which ->
                // 💡 CORE CHANGE: Use ViewModel to delete data by ID
                lifecycleScope.launch {
                    transactionViewModel.deleteTransaction(transaction.id)
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, which ->
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