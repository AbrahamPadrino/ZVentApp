package com.example.z_ventapp.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Empresa
import com.example.z_ventapp.domain.usecase.empresa.ObtenerEmpresaUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeFlowCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuscarEmpresaViewModel @Inject constructor(
    private val obtenerEmpresaUseCase: ObtenerEmpresaUseCase
): ViewModel() {

    private val _itemEmpresa = MutableLiveData<Empresa?>()
    val itemEmpresa: LiveData<Empresa?> = _itemEmpresa

    private val _uiStateEmpresa = MutableStateFlow<UiState<Empresa?>?>(null)
    val uiStateEmpresa = _uiStateEmpresa.asStateFlow()

    fun resetUiStateEmpresa() {
        _uiStateEmpresa.value = null
    }

    fun obtenerEmpresa() = viewModelScope.launch {
        _uiStateEmpresa.value = UiState.Loading

        makeFlowCall { obtenerEmpresaUseCase() }.collect {
            if(it is UiState.Success)
                _itemEmpresa.value = it.data

            _uiStateEmpresa.value = it
        }
    }
}