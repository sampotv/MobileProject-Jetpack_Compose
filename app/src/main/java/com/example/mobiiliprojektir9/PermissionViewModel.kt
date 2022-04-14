package com.example.mobiiliprojektir9

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionViewModel : ViewModel() {
    private val _performCameraAction: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val performCameraAction = _performCameraAction.asStateFlow()

    fun setPerformCameraAction(request: Boolean){
        _performCameraAction.value = request
    }
}