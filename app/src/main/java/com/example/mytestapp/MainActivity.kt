package com.example.mytestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var helloMessageView: TextView
    private lateinit var removeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the traditional XML layout
        setContentView(R.layout.activity_main)

        // Find views by ID from the layout
        // Note: findViewById must be called after setContentView
        helloMessageView = findViewById(R.id.hello_message_view)
        removeButton = findViewById(R.id.remove_button)
        val helloButton: Button = findViewById(R.id.hello_button)

        // Set up listeners
        setupListeners(helloButton)
    }

    private fun setupListeners(helloButton: Button) {
        // Handle the 'Hello World' button (green) click
        helloButton.setOnClickListener {
            // Check if the message view is not visible, implying this is the first press
            if (helloMessageView.visibility != View.VISIBLE) {
                // Make the message view visible and set the text
                helloMessageView.visibility = View.VISIBLE
                helloMessageView.text = "Hello World Message Created!"
            }
            Toast.makeText(this, "Hello button pressed. Message is displayed/acknowledged.", Toast.LENGTH_SHORT).show()
        }

        // Handle the 'Remove Message' button (blue) click
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