package com.example.mytestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytestapp.ui.theme.MyTestAppTheme
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTestAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                Toast.makeText(context, "hello World", Toast.LENGTH_LONG).show()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            // Using a generic button if Compose specific imports are complex,
            // but sticking to Material3 components is better.
            // Since I don't have access to the theme context, I'll use a standard Button.
            // Note: For a full theme implementation, setup is needed, but for a minimal test, this should suffice.
            // Assuming necessary imports for a functional Compose environment are available contextually.
        }
    }
}