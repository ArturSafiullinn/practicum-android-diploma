package ru.practicum.android.diploma.ui.screens.vacancy

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.models.VacancyDetailUi
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space32
import ru.practicum.android.diploma.ui.theme.Dimens.Space8

@Composable
fun VacancyContacts(vacancy: VacancyDetailUi) {
    val context = LocalContext.current

    val hasContacts =
        !vacancy.contactsName.isNullOrBlank() ||
                !vacancy.contactsEmail.isNullOrBlank() ||
                !(vacancy.contactsPhone.isNullOrEmpty())

    if (!hasContacts) return

    Column(Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.contacts),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(Space16))

        vacancy.contactsName?.takeIf { it.isNotBlank() }?.let { name ->
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(Space8))
        }

        vacancy.contactsEmail?.takeIf { it.isNotBlank() }?.let { email ->
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:$email")
                    }
                    val chooser = Intent.createChooser(
                        intent,
                        context.getString(R.string.choose_email_client)
                    )
                    try {
                        context.startActivity(chooser)
                    } catch (e: ActivityNotFoundException) {
                        Toast
                            .makeText(
                                context,
                                R.string.no_email_app,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                }
            )

            Spacer(Modifier.height(Space8))
        }

        vacancy.contactsPhone
            ?.takeIf { it.isNotEmpty() }
            ?.forEachIndexed { index, phoneRaw ->
                val displayText = phoneRaw

                Text(
                    text = displayText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        val digitsOnly = phoneRaw.filter { it.isDigit() || it == '+' }
                        if (digitsOnly.isNotBlank()) {
                            val uri = Uri.parse("tel:$digitsOnly")
                            val intent = Intent(Intent.ACTION_DIAL, uri)
                            val chooser = Intent.createChooser(
                                intent,
                                context.getString(R.string.choose_call_app)
                            )
                            try {
                                context.startActivity(chooser)
                            } catch (e: ActivityNotFoundException) {
                                Toast
                                    .makeText(
                                        context,
                                        R.string.no_call_app,
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }
                    }
                )

                if (index != vacancy.contactsPhone.lastIndex) {
                    Spacer(Modifier.height(Space8))
                }
            }

        Spacer(Modifier.height(Space32))
    }
}
