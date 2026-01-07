package com.airss.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airss.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: Repository) : ViewModel() {
    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean> = _refreshing

    fun refresh() {
        if (_refreshing.value) return
        _refreshing.value = true
        viewModelScope.launch {
            try {
                repo.refreshAll()
            } finally {
                _refreshing.value = false
            }
        }
    }
}
