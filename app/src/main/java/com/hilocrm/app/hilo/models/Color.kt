package com.hilocrm.app.hilo.models

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import com.hilocrm.app.hilo.R

/**
 * Created by eduardalbu on 03.02.2018.
 */
enum class NoteColor {
    white, red, rose, purple, dark_blue, blue, sky_blue, olive, green,
    chartreuse, yellow, dark_yellow, brown, grey;

    companion object {
        fun fromString(stringColor: String = "White"): NoteColor = when (stringColor) {
            "Red" -> red
            "Rose" -> rose
            "Purple" -> purple
            "Dark Blue" -> dark_blue
            "Blue" -> blue
            "Sky Blue" -> sky_blue
            "Olive" -> olive
            "Green" -> green
            "Chartreuse" -> chartreuse
            "Yellow" -> yellow
            "Dark Yellow" -> dark_yellow
            "Brown" -> brown
            "Grey" -> grey
            else -> white
        }
    }

    @ColorRes
    fun colorRes(): Int = when (this) {
        white -> R.color.colorWhite
        red -> R.color.colorRed
        rose -> R.color.colorRose
        purple -> R.color.colorPurple
        dark_blue -> R.color.colorAccent
        blue -> R.color.colorBlue
        sky_blue -> R.color.colorSkyBlue
        olive -> R.color.colorOlive
        green -> R.color.colorGreen
        chartreuse -> R.color.colorChartreuse
        yellow -> R.color.colorYellow
        dark_yellow -> R.color.colorDarkYellow
        brown -> R.color.colorBrown
        grey -> R.color.colorGray
    }

    @DrawableRes
    fun deleteIcon(): Int = when (this) {
        white, chartreuse, dark_yellow, yellow -> R.drawable.ic_action_delete_empty

        red, rose, purple, dark_blue, blue, sky_blue, olive, green, brown, grey -> R.drawable.ic_action_delete_white
    }

    @DrawableRes
    fun editIcon(): Int = when (this) {
        white, chartreuse, dark_yellow, yellow -> R.drawable.ic_action_edit

        red, rose, purple, dark_blue, blue, sky_blue, olive, green, brown, grey -> R.drawable.ic_action_edit_white
    }

    @DrawableRes
    fun tagIcon(): Int = when (this) {
        white, chartreuse, dark_yellow, yellow -> R.drawable.ic_action_add_tag

        red, rose, purple, dark_blue, blue, sky_blue, olive, green, brown, grey -> R.drawable.ic_action_add_tag_white
    }

    @DrawableRes
    fun colorIcon(): Int = when (this) {
        white, chartreuse, dark_yellow, yellow -> R.drawable.ic_action_pick_color

        red, rose, purple, dark_blue, blue, sky_blue, olive, green, brown, grey -> R.drawable.ic_action_pick_color_white
    }

    fun stringColor(): String = when (this) {
        white -> "White"
        red -> "Red"
        rose -> "Rose"
        purple -> "Purple"
        dark_blue -> "Dark Blue"
        blue -> "Blue"
        sky_blue -> "Sky Blue"
        olive -> "Olive"
        green -> "Green"
        chartreuse -> "Chartreuse"
        yellow -> "Yellow"
        dark_yellow -> "Dark Yellow"
        brown -> "Brown"
        grey -> "Grey"
    }

    @ColorRes
    fun textColor(): Int = when (this) {
        white, chartreuse, dark_yellow, yellow -> android.R.color.black

        red, rose, purple, dark_blue, blue, sky_blue, olive, green,
        brown, grey -> R.color.colorWhite
    }
}


enum class TagColor {
    red, purple, deep_purple, indigo, blue, light_blue_600, pink, cyan_700, teal, green_600, light_green_700,
    lime_700, yellow_900, yellow_800, amber_900, orange_900, deep_orange_900, deep_orange_a400, deep_orange,
    blue_grey_900, blue_grey, brown_900, grey_800, amber_a700, purple_a400, grey_600, teal_900, green_a400, white;

    companion object {
        fun fromString(stringColor: String): TagColor = when(stringColor) {
            "md-bg-red-500" -> red
            "md-bg-purple-500" -> purple
            "md-bg-deep-purple-500" -> deep_purple
            "md-bg-indigo-500" -> indigo
            "md-bg-blue-500" -> blue
            "md-bg-light-blue-600" -> light_blue_600
            "md-bg-pink-500" -> pink
            "md-bg-cyan-700" -> cyan_700
            "md-bg-teal-500" -> teal
            "md-bg-green-600" -> green_600
            "md-bg-light-green-700" -> light_green_700
            "md-bg-lime-700" -> lime_700
            "md-bg-yellow-900" -> yellow_900
            "md-bg-yellow-800" -> yellow_800
            "md-bg-amber-900" -> amber_900
            "md-bg-orange-900" -> orange_900
            "md-bg-deep-orange-900" -> deep_orange_900
            "md-bg-deep-orange-A400" -> deep_orange_a400
            "md-bg-deep-orange-500" -> deep_orange
            "md-bg-blue-grey-900" -> blue_grey_900
            "md-bg-blue-grey-500" -> blue_grey
            "md-bg-brown-900" -> brown_900
            "md-bg-grey-800" -> grey_800
            "md-bg-amber-A700" -> amber_a700
            "md-bg-purple-A400" -> purple_a400
            "md-bg-grey-600" -> grey_600
            "md-bg-teal-900" -> teal_900
            "md-bg-green-A400" -> green_a400
            else -> white
        }
    }

    @ColorRes
    fun colorRes(): Int = when(this) {
        red -> R.color.tagRed
        purple -> R.color.tagPurple
        deep_purple -> R.color.tagDeepPurple
        indigo -> R.color.tagIndigo
        blue -> R.color.tagBlue
        light_blue_600 -> R.color.tagLightBlue600
        pink -> R.color.tagPink
        cyan_700 -> R.color.tagCyan700
        teal -> R.color.tagTeal
        green_600 -> R.color.tagGreen600
        light_green_700 -> R.color.tagLightGreen700
        lime_700 -> R.color.tagLime700
        yellow_900 -> R.color.tagYellow900
        yellow_800 -> R.color.tagYellow800
        amber_900 -> R.color.tagAmber900
        orange_900 -> R.color.tagOrange900
        deep_orange_900 -> R.color.tagDeepOrange900
        deep_orange_a400 -> R.color.tagDeepOrangeA400
        deep_orange -> R.color.tagDeepOrange
        blue_grey_900 -> R.color.tagBlueGrey900
        blue_grey -> R.color.tagBlueGrey
        brown_900 -> R.color.tagBrown900
        grey_800 -> R.color.tagGrey800
        amber_a700 -> R.color.tagAmberA700
        purple_a400 -> R.color.tagPurpleA400
        grey_600 -> R.color.tagGrey600
        teal_900 -> R.color.tagTeal900
        green_a400 -> R.color.tagGreenA400
        white -> R.color.colorWhite
    }

    @ColorRes
    fun textColor(): Int = when(this) {
        red, purple, deep_purple, indigo, blue, light_blue_600, pink, cyan_700, teal, green_600, light_green_700,
        lime_700, yellow_900, yellow_800, amber_900, orange_900, deep_orange_900, deep_orange_a400, deep_orange,
        blue_grey_900, blue_grey, brown_900, grey_800, amber_a700, purple_a400, grey_600, teal_900, green_a400 -> R.color.colorWhite
        white -> android.R.color.black
    }

    @DrawableRes
    fun icon(): Int = when(this) {
        red, purple, deep_purple, indigo, blue, light_blue_600, pink, cyan_700, teal, green_600, light_green_700,
        lime_700, yellow_900, yellow_800, amber_900, orange_900, deep_orange_900, deep_orange_a400, deep_orange,
        blue_grey_900, blue_grey, brown_900, grey_800, amber_a700, purple_a400, grey_600, teal_900, green_a400 -> R.drawable.ic_close_white_18dp
        white -> R.drawable.ic_close_black_18dp
    }
}
