package fr.group5.magellangpt.common.helpers

import androidx.annotation.StringRes

interface ResourcesHelper {
    fun getString(@StringRes stringId: Int, vararg formatArgs: Any) : String
}