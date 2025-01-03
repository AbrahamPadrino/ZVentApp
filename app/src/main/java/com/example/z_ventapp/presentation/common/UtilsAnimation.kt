package com.example.z_ventapp.presentation.common

import android.graphics.Color
import android.view.View
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform

object UtilsAnimation {

    // Transformar el FAB en un menú de opciones
    fun crearTransformacion(
        vistaInicial: View,
        vistaFinal: View,
        duracion: Long = 500L,
        colorFondoTransicion: Int = Color.TRANSPARENT
    ): MaterialContainerTransform {
        return MaterialContainerTransform().apply {
            startView = vistaInicial
            endView = vistaFinal
            duration = duracion
            scrimColor = colorFondoTransicion
            setPathMotion(MaterialArcMotion())
            addTarget(vistaFinal)
        }

    }
}