package fr.group5.magellangpt.common.helpers.implementations

import android.content.Context
import android.content.SharedPreferences
import fr.group5.magellangpt.common.helpers.PreferencesHelper
import org.koin.java.KoinJavaComponent.get

class PreferencesHelperImpl(context: Context = get(Context::class.java)) : BasePreferencesHelperImpl(),
    PreferencesHelper {

    override val sharedPreferences: SharedPreferences
            = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)


    companion object {
        private const val SHARED_PREFERENCES_NAME = "shared_preferences"

        private const val PREFERENCE_KEY_FIRSTNAME = "firstname_preference_key"
        private const val PREFERENCE_KEY_LASTNAME = "username_preference_key"
        private const val PREFERENCE_KEY_MAIL = "mail_preference_key"
    }

    override var lastname: String
        get() = getValue(PREFERENCE_KEY_FIRSTNAME, "")
        set(value) = setValue(PREFERENCE_KEY_FIRSTNAME, value)

    override var firstname: String
        get() = getValue(PREFERENCE_KEY_LASTNAME, "")
        set(value) = setValue(PREFERENCE_KEY_LASTNAME, value)

    override var mail: String
        get() = getValue(PREFERENCE_KEY_MAIL, "")
        set(value) = setValue(PREFERENCE_KEY_MAIL, value)
}