package com.example.z_ventapp.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Usuario
import com.example.z_ventapp.domain.usecase.usuario.ExisteCuentaUsuarioUseCase
import com.example.z_ventapp.domain.usecase.usuario.ObtenerUsuarioUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val obtenerUsuarioUseCase: ObtenerUsuarioUseCase,
    private val  existeCuentaUsuarioUseCase: ExisteCuentaUsuarioUseCase
): ViewModel(){
    // Manejador de estado
    private val _uiStateLogin = MutableLiveData<UiState<Usuario?>?>()
    val uiStateLogin: LiveData<UiState<Usuario?>?> = _uiStateLogin

    private val _uiStateExisteCuenta = MutableLiveData<UiState<Int>?>()
    val uiStateExisteCuenta: LiveData<UiState<Int>?> = _uiStateExisteCuenta
    // Reseteo de estado
    fun resetUiStateLogin() {
        _uiStateLogin.value = null
    }

    fun resetUiStateExisteCuenta() {
        _uiStateExisteCuenta.value = null
    }
    // Llamada a la capa de casos de uso
    fun login(email: String, password: String) = viewModelScope.launch {

        _uiStateLogin.value = UiState.Loading
        makeCall { obtenerUsuarioUseCase(email, password) }.let {
            _uiStateLogin.value = it
        }

    }
    // Llamada a la capa de casos de uso
    fun existeCuenta() = viewModelScope.launch {

        _uiStateExisteCuenta.value = UiState.Loading
        makeCall { existeCuentaUsuarioUseCase() }.let {
            _uiStateExisteCuenta.value = it
        }
    }




}