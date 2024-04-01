package fr.group5.magellangpt.common.helpers.implementations

import android.content.Context
import androidx.annotation.StringRes
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import org.koin.java.KoinJavaComponent.get

class ResourcesHelperImpl(
    private val context: Context = get(Context::class.java),
) : ResourcesHelper {
    override fun getString(@StringRes stringId: Int, vararg formatArgs: Any): String
            = context.getString(stringId, *formatArgs)
}