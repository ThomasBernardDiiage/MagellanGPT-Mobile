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

        private const val PREFERENCE_KEY_ACCESS_TOKEN = "access_token_preference_key"
        private const val PREFERENCE_KEY_SELECTED_MODEL_ID = "selected_model_id_preference_key"
    }

    override var accessToken: String
        get() = getValue(PREFERENCE_KEY_ACCESS_TOKEN, "")
        set(value) = setValue(PREFERENCE_KEY_ACCESS_TOKEN, value)


    override var selectedModelId: String
        get() = getValue(PREFERENCE_KEY_SELECTED_MODEL_ID, "")
        set(value) = setValue(PREFERENCE_KEY_SELECTED_MODEL_ID, value)
}