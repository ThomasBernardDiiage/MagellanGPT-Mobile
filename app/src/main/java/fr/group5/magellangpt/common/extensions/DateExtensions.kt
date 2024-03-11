package fr.group5.magellangpt.common.extensions

import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import org.koin.java.KoinJavaComponent.get
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val resourcesHelper : ResourcesHelper = get(ResourcesHelper::class.java)

fun Date.toPrettyDate() : String {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH)
    return dateFormatter.format(this)
}

// Return today, yesterday or the date
fun Date.toDateLabel() : String {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
    val today = dateFormatter.format(Date())
    val yesterday = dateFormatter.format(Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24))
    val date = dateFormatter.format(this)

    return when (date) {
        today -> resourcesHelper.getString(R.string.today)
        yesterday -> resourcesHelper.getString(R.string.yersterday)
        else -> date
    }
}


fun Date.to6AM() : Date {
    val calendar = java.util.Calendar.getInstance()
    calendar.time = this
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 6)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    return calendar.time
}