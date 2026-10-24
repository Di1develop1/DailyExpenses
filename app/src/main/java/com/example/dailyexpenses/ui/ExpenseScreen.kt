package com.example.dailyexpenses.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailyexpenses.viewmodel.ExpenseViewModel

@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel = viewModel()) {
    val expenses by viewModel.expenses.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val titleInput by viewModel.titleInput.collectAsState()
    val amountInput by viewModel.amountInput.collectAsState()
    val showDialogFor by viewModel.showDialogFor.collectAsState()

    val parsedAmount = amountInput.toDoubleOrNull() ?: 0.0
    val isButtonEnabled = titleInput.isNotBlank() && parsedAmount > 0

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Витрати за день",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Разом: ", fontSize = 16.sp, color = Color.Gray)
                Text(
                    text = "₴ ${if (totalAmount % 1.0 == 0.0) totalAmount.toInt() else totalAmount}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE53935)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { viewModel.onTitleChange(it) },
                    placeholder = { Text("Назва") },
                    modifier = Modifier.weight(2f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { viewModel.onAmountChange(it) },
                    placeholder = { Text("Сума") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.addExpense() },
                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),
                    disabledContainerColor = Color(0xFFFFCDD2)
                )
            ) {
                Text(
                    text = "+ Додати витрату",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = expenses, key = { it.id }) { expense ->
                    ExpenseCard(
                        expense = expense,
                        onClick = { viewModel.requestDeleteExpense(expense.id) }
                    )
                }
            }
        }

        if (showDialogFor != null) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDeleteDialog() },
                title = { Text("Видалення витрати") },
                text = { Text("Ви дійсно бажаєте видалити цей запис?") },
                confirmButton = {
                    TextButton(onClick = { viewModel.confirmDeleteExpense() }) {
                        Text("Видалити", color = Color(0xFFE53935))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                        Text("Скасувати")
                    }
                }
            )
        }
    }
}