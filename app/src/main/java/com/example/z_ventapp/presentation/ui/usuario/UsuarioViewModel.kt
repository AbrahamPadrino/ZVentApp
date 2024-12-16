package com.example.z_ventapp.presentation.ui.usuario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Usuario
import com.example.z_ventapp.domain.usecase.usuario.EliminarUsuarioUseCase
import com.example.z_ventapp.domain.usecase.usuario.ListarUsuarioUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val listarUsuarioUseCase: ListarUsuarioUseCase,
    private val eliminarUsuarioUseCase: EliminarUsuarioUseCase
) : ViewModel() {
    // Manejador de estado
    private val _uiStateListar = MutableStateFlow<UiState<List<Usuario>>?>(null)
    val uiStateListar: StateFlow<UiState<List<Usuario>>?> = _uiStateListar

    private val _uiStateEliminar = MutableLiveData<UiState<Int>?>()
    val uiStateEliminar: LiveData<UiState<Int>?> = _uiStateEliminar
    //

    // Resetear los estados
    fun resetUiStateListar() {
        _uiStateListar.value = null
    }

    fun resetUiStateEliminar() {
        _uiStateEliminar.value = null
    }

    //
    fun listar(dato: String) = viewModelScope.launch {
        _uiStateListar.value = UiState.Loading

        listarUsuarioUseCase(dato)
            .flowOn(Dispatchers.IO)
            .catch {
                _uiStateListar.value = UiState.Error(it.message.orEmpty())
            }
            .collect {
                _uiStateListar.value = UiState.Success(it)
            }
    }

    fun eliminar(entidad: Usuario) = viewModelScope.launch {
        _uiStateEliminar.value = UiState.Loading

        makeCall { eliminarUsuarioUseCase(entidad) }.let {
            _uiStateEliminar.value = it
        }
    }
}