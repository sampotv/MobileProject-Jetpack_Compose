package com.example.mobiiliprojektir9

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionViewModel : ViewModel() {
    private val _performCameraAction: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private var _id: MutableStateFlow<String> = MutableStateFlow("")
    val performCameraAction = _performCameraAction.asStateFlow()
    var id = _id.asStateFlow()

    fun setPerformCameraAction(request: Boolean){
        _performCameraAction.value = request
    }
    fun setId(request: String){
        _id.value = request
    }
}