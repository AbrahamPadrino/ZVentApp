package com.example.z_ventapp.presentation.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.net.UnknownHostException

/**
 * Función suspendida que transforma un flujo (`Flow`) de datos en un flujo de estados de interfaz de usuario (`UiState`).
 *
 * Esta función toma una función lambda `call` que retorna un `Flow` de tipo `T`. El flujo resultante de `call` es
 * transformado en un flujo de estados de la interfaz de usuario (`UiState<T>`). El estado puede ser de éxito o error.
 *
 * - `Success`: Representa un estado exitoso que contiene los datos del flujo.
 * - `Error`: Representa un estado de error que contiene el mensaje de error en caso de que ocurra una excepción.
 *
 * @param call Una función lambda que retorna un `Flow<T>`, donde `T` es el tipo de los datos.
 * @return Un `Flow<UiState<T>>` que emite `UiState.Success` con los datos del flujo en caso de éxito o `UiState.Error`
 * con el mensaje de error en caso de excepción.
 *
 * @author Jack Chavez Saravia.
 */
suspend fun <T> makeFlowCall(call: () -> Flow<T>): Flow<UiState<T>> {
    return call()
        .map {
            UiState.Success(it) as UiState<T>
        }
        .flowOn(Dispatchers.IO)
        .catch {
            //emit(UiState.Error(it.message.toString()))
            when (it) {
                is UnknownHostException ->
                    emit(UiState.Error("No hay conexión a internet: ${it.message.orEmpty()}"))

                is IOException ->
                    emit(UiState.Error("Error de red: ${it.message.orEmpty()}"))

                else ->
                    emit(UiState.Error(it.message.toString()))
            }
        }
}