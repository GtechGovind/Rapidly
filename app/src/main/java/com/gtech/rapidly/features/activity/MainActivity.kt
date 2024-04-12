package com.gtech.rapidly.features.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import cafe.adriel.voyager.navigator.Navigator
import com.gtech.rapidly.features.common.ui.utils.WithTheme

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var instance: MainActivity
            private set
    }

    val upiPaymentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Payment completed successfully
            // Extract transaction ID from result data
            val data: Intent? = result.data
            val transactionId = data?.getStringExtra("txnId")
            Toast.makeText(this, "Transaction ID: $transactionId", Toast.LENGTH_SHORT).show()
        } else {
            // Payment failed or was canceled
            // Handle the situation accordingly
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        enableEdgeToEdge()
        setContent {
            WithTheme {
                Navigator(MainScreen())
            }
        }
    }

}