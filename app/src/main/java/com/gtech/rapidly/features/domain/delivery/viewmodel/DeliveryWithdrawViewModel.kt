package com.gtech.rapidly.features.domain.delivery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.gtech.rapidly.features.common.firestore.model.Withdraw
import com.gtech.rapidly.features.common.firestore.service.WithdrawalService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeliveryWithdrawViewModel : ScreenModel() {

    var amount by mutableStateOf(TextFieldValue())
    var note by mutableStateOf(TextFieldValue())

    fun submit() = screenModelScope.launch(
        Dispatchers.IO
    ) {
        withLoading {
            val withdraw = getAndValidate()
            if (withdraw != null) {
                if (WithdrawalService.saveOrUpdate(withdraw)) {
                    showMessage("Withdraw request submitted")
                    amount = TextFieldValue()
                    note = TextFieldValue()
                } else {
                    showMessage("Failed to submit withdraw request")
                }
            }
        }
    }

    private suspend fun getAndValidate(): Withdraw? {

        val amount = amount.text.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            showMessage("Please enter a valid amount")
            return null
        }

        val note = note.text
        if (note.isBlank()) {
            showMessage("Please enter a note")
            return null
        }

        return Withdraw(
            requestAmount = amount,
            requestNote = note,
            requestedBy = RuntimeCache.getCurrentUser().phoneNumber
        )

    }

    fun markAsSeen(
        withdraw: Withdraw
    ) = screenModelScope.launch(Dispatchers.IO) {
        withdraw.isSeen = true
        if (!WithdrawalService.saveOrUpdate(withdraw)) {
            showMessage("failed to mark as seen!")
        }
    }

}