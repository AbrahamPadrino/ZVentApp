package com.example.z_ventapp.presentation.ui.usuario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Usuario
import com.example.z_ventapp.domain.usecase.usuario.GrabarUsuarioUseCase
import com.example.z_ventapp.domain.usecase.usuario.ObtenerUsuarioPorIdUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OperacionUsuarioViewModel @Inject constructor(
    private val grabarUsuarioUseCase: GrabarUsuarioUseCase,
    private val obtenerUsuarioPorIdUseCase: ObtenerUsuarioPorIdUseCase
) : ViewModel() {
    // Manejador de estados
    private val _uiStateGrabar = MutableLiveData<UiState<Int>?>()
    val uiStateGrabar: LiveData<UiState<Int>?> = _uiStateGrabar

    private val _uiStateObtenerPorId = MutableLiveData<UiState<Usuario?>?>()
    val uiStateObtenerPorId: LiveData<UiState<Usuario?>?> = _uiStateObtenerPorId

    //
    fun resetUiStateGrabar() {
        _uiStateGrabar.value = null
    }

    fun resetUiStateObtenerPorId() {
        _uiStateObtenerPorId.value = null
    }

    fun grabar(entidad: Usuario) = viewModelScope.launch {
        _uiStateGrabar.value = UiState.Loading

        makeCall { grabarUsuarioUseCase(entidad) }.let {
            _uiStateGrabar.value = it
        }
    }

    fun obtenerPorId(id: Int) = viewModelScope.launch {

        _uiStateObtenerPorId.value = UiState.Loading

        makeCall { obtenerUsuarioPorIdUseCase(id) }.let {
            _uiStateObtenerPorId.value = it
        }
    }

}