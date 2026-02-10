package ru.practicum.android.diploma.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

import ru.practicum.android.diploma.R

fun formatSalarySpaced(amount: Int?): String {
    return amount?.let {
        "%,d".format(it).replace(',', ' ')
    } ?: ""
}

fun openEmail(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email")
    }

    try {
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.choose_email_client)
            )
        )
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, R.string.no_email_app, Toast.LENGTH_SHORT).show()
    }
}

fun openPhone(context: Context, phoneRaw: String) {
    val digitsOnly = phoneRaw.filter { it.isDigit() || it == '+' }
    if (digitsOnly.isBlank()) return

    val intent = Intent(
        Intent.ACTION_DIAL,
        Uri.parse("tel:$digitsOnly")
    )

    try {
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.choose_call_app)
            )
        )
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, R.string.no_call_app, Toast.LENGTH_SHORT).show()
    }
}
