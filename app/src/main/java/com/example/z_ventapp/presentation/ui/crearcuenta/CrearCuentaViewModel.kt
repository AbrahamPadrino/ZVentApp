package com.example.z_ventapp.presentation.ui.crearcuenta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Usuario
import com.example.z_ventapp.domain.usecase.usuario.GrabarCuentaUsuarioUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
/** ViewModel encargado de comunicarse con los casos de uso y a su vez interactuar
 * con la interfaz grafica a traves de los modificadores de estado.  */
@HiltViewModel
class CrearCuentaViewModel @Inject constructor(
    private val useCase: GrabarCuentaUsuarioUseCase
) : ViewModel() {

    private val _uiStateGrabar = MutableLiveData<UiState<Usuario?>?>()
    val uiStateGrabar: LiveData<UiState<Usuario?>?> = _uiStateGrabar

    fun resetUiStateGrabar() {
        _uiStateGrabar.value = null
    }

    fun grabarCuenta(usuario: Usuario) = viewModelScope.launch {
        _uiStateGrabar.value = UiState.Loading

        makeCall { useCase(usuario) }.let {
            _uiStateGrabar.value = it
        }
    }

}