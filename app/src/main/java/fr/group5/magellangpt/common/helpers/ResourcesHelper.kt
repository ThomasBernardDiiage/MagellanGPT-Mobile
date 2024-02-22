package fr.group5.magellangpt.common.helpers

import androidx.annotation.StringRes

interface ResourcesHelper {
    fun getString(@StringRes stringId: Int, formatArgs: Any? = null) : String
}