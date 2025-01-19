package com.example.z_ventapp.presentation.ui.cambiarclave

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.usecase.usuario.ActualizarClaveUsuarioUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CambiarClaveViewModel @Inject constructor(
    private val actualizarClaveUsuarioUseCase: ActualizarClaveUsuarioUseCase
) : ViewModel() {
    //Manejador de estado
    private val _uiState = MutableLiveData<UiState<Int>?>(null)
    val uiState: MutableLiveData<UiState<Int>?> = _uiState

    // Reset del estado
    fun resetUiState() { _uiState.value = null }

    // Function para actualizar la clave
    fun actualizarClave(id: Int, clave: String) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        makeCall { actualizarClaveUsuarioUseCase(id, clave) }.let {
            _uiState.value = it
        }
    }

}