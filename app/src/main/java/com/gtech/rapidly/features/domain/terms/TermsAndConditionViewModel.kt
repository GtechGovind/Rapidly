package com.gtech.rapidly.features.domain.terms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gtech.rapidly.features.common.firestore.model.Legal
import com.gtech.rapidly.features.common.firestore.service.LegalService
import com.gtech.rapidly.features.common.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TermsAndConditionViewModel : ViewModel() {

    var terms: List<Legal> by mutableStateOf(emptyList())

    override suspend fun onCreated() {
        super.onCreated()
        withContext(Dispatchers.IO) {
            withLoading {
                terms = LegalService
                    .getAll()
                    .sortedBy { it.id }
            }
        }
    }

}