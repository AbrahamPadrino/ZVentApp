package com.example.z_ventapp.presentation.common
/** clase para manejar lor estado de la UI */

sealed class UiState<out T> {
    data class Success<T>(val data: T): UiState<T>()
    data class Error<T>(val message: String): UiState<T>()
    data object Loading: UiState<Nothing>()
}
