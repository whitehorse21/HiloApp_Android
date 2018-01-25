package com.hiloipa.app.hilo.widget

/**
 * Created by eduardalbu on 25.01.2018.
 */
enum class FontType {
    bold, light, medium, regular, semibold;

    companion object {
        fun fromInt(intValue: Int): FontType = when(intValue) {
            1 -> bold
            2 -> light
            3 -> medium
            4 -> semibold
            else -> regular
        }
    }

    fun fontName(): String = when(this) {
        bold -> "Raleway-Bold.ttf"
        light -> "Raleway-Light.ttf"
        medium -> "Raleway-Medium.ttf"
        regular -> "Raleway-Regular.ttf"
        semibold -> "Raleway-SemiBold.ttf"
    }
}