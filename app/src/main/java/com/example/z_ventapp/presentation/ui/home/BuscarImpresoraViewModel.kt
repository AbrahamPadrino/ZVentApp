package com.example.z_ventapp.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Impresora
import com.example.z_ventapp.domain.usecase.impresora.ObtenerImpresoraUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeFlowCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuscarImpresoraViewModel @Inject constructor(

    private val obtenerImpresoraUseCase: ObtenerImpresoraUseCase

) : ViewModel() {

    private val _itemImpresora = MutableLiveData<Impresora?>()
    val itemImpresora: LiveData<Impresora?> = _itemImpresora

    private val _uiStateImpresora = MutableStateFlow<UiState<Impresora?>?>(null)
    val uiStateImpresora = _uiStateImpresora.asStateFlow()

    fun resetUiStateImpresora() {
        _uiStateImpresora.value = null
    }

    fun obtenerImpresora() = viewModelScope.launch {
        _uiStateImpresora.value = UiState.Loading

        makeFlowCall { obtenerImpresoraUseCase() }.collect {
            if(it is UiState.Success)
                _itemImpresora.value=it.data

            _uiStateImpresora.value = it
        }
    }
}