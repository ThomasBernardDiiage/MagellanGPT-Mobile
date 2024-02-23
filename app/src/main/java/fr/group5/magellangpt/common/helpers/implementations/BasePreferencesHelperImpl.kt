package fr.group5.magellangpt.common.helpers.implementations

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

abstract class BasePreferencesHelperImpl {

    protected abstract val sharedPreferences: SharedPreferences
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss'Z'", Locale.FRENCH)


    protected fun getValue(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    protected fun setValue(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    protected fun getValue(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    protected fun setValue(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    protected fun getValue(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    protected fun setValue(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }


    protected fun getValue(key: String, defaultValue: Date): Date {
        val defaultValueString = dateFormatter.format(defaultValue)
        val dateString = sharedPreferences.getString(key, defaultValueString) ?: defaultValueString
        return dateFormatter.parse(dateString) ?: defaultValue
    }

    protected fun setValue(key: String, value: Date) {
        val dateString = dateFormatter.format(value)
        sharedPreferences.edit().putString(key, dateString).apply()
    }
}