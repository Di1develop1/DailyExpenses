package com.example.dailyexpenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyexpenses.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ExpenseViewModel : ViewModel() {

    private var currentId = 0

    private val _expenses = MutableStateFlow<List<Expense>>(
        listOf(
            Expense(id = ++currentId, title = "Кава", amount = 65.0),
            Expense(id = ++currentId, title = "Проїзд", amount = 24.0),
            Expense(id = ++currentId, title = "Обід", amount = 320.0),
            Expense(id = ++currentId, title = "Продукти", amount = 438.0)
        )
    )
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    val totalAmount: StateFlow<Double> = _expenses
        .map { list -> list.sumOf { it.amount } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    var titleInput = MutableStateFlow("")
        private set

    var amountInput = MutableStateFlow("")
        private set

    private val _showDialogFor = MutableStateFlow<Int?>(null)
    val showDialogFor: StateFlow<Int?> = _showDialogFor.asStateFlow()

    fun onTitleChange(newTitle: String) {
        titleInput.value = newTitle
    }

    fun onAmountChange(newAmount: String) {
        if (newAmount.isEmpty() || newAmount.matches(Regex("^\\d*(\\.\\d*)?$"))) {
            amountInput.value = newAmount
        }
    }

    fun addExpense() {
        val amount = amountInput.value.toDoubleOrNull() ?: 0.0
        val title = titleInput.value.trim()

        if (title.isNotEmpty() && amount > 0) {
            val newItem = Expense(
                id = ++currentId,
                title = title,
                amount = amount
            )
            _expenses.value = _expenses.value + newItem
            titleInput.value = ""
            amountInput.value = ""
        }
    }

    fun requestDeleteExpense(id: Int) {
        _showDialogFor.value = id
    }

    fun dismissDeleteDialog() {
        _showDialogFor.value = null
    }

    fun confirmDeleteExpense() {
        _showDialogFor.value?.let { idToDelete ->
            _expenses.value = _expenses.value.filterNot { it.id == idToDelete }
        }
        _showDialogFor.value = null
    }
}