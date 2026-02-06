package ru.practicum.android.diploma.presentation.impl

import android.content.Context
import android.content.Intent
import ru.practicum.android.diploma.presentation.api.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
        }
        val chooserIntent = Intent.createChooser(sharingIntent, null)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(chooserIntent)
    }
}
