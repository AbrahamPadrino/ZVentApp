package com.example.z_ventapp.presentation.common


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

/**
 * Es una funcion generica para ejecutar las funciones suspendidas de una manera centralizada.
 *
 * Esta funcion generica llamaca makeCall, recibe como parametro una funcion
 * suspendida (generica) y devuelve un objeto de tipo UiState<T>.
 * Este se ejecuta en un hilo secundario.
 *
 * @param call recibe una llamada suspendida (generica)
 * @return UiState<T>.
 * @author Abraham Dev.
 */
suspend fun <T> makeCall(call: suspend () -> T): UiState<T> {
    return withContext(Dispatchers.IO) {
        try {
            UiState.Success(call())
        } catch (e: UnknownHostException) {
            UiState.Error(e.message.orEmpty())
        } catch (e: Exception) {
            UiState.Error(e.message.orEmpty())
        }
    }
}