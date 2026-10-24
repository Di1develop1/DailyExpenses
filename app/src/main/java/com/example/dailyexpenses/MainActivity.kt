package com.example.dailyexpenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dailyexpenses.ui.ExpenseScreen
import com.example.dailyexpenses.ui.theme.DailyExpensesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyExpensesTheme {
                ExpenseScreen()
            }
        }
    }
}