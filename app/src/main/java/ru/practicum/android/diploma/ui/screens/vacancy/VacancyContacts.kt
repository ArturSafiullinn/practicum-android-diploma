package ru.practicum.android.diploma.ui.screens.vacancy

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
import ru.practicum.android.diploma.util.openEmail
import ru.practicum.android.diploma.util.openPhone

@Composable
fun VacancyContacts(vacancy: VacancyDetailUi) {
    val context = LocalContext.current

    val hasContacts =
        !vacancy.contactsName.isNullOrBlank() ||
            !vacancy.contactsEmail.isNullOrBlank() ||
            !vacancy.contactsPhone.isNullOrEmpty()

    if (!hasContacts) return

    Column(Modifier.fillMaxWidth()) {
        ContactsHeader()

        vacancy.contactsName
            ?.takeIf { it.isNotBlank() }
            ?.let { ContactName(it) }

        vacancy.contactsEmail
            ?.takeIf { it.isNotBlank() }
            ?.let { email ->
                ContactEmail(email) {
                    openEmail(context, email)
                }
            }

        vacancy.contactsPhone
            ?.takeIf { it.isNotEmpty() }
            ?.let { phones ->
                ContactPhones(phones) { phone ->
                    openPhone(context, phone)
                }
            }

        Spacer(Modifier.height(Space32))
    }
}

@Composable
private fun ContactsHeader() {
    Text(
        text = stringResource(R.string.contacts),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.height(Space16))
}

@Composable
private fun ContactName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.height(Space8))
}

@Composable
private fun ContactEmail(
    email: String,
    onClick: () -> Unit
) {
    Text(
        text = email,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.clickable(onClick = onClick)
    )
    Spacer(Modifier.height(Space8))
}

@Composable
private fun ContactPhones(
    phones: List<String>,
    onClick: (String) -> Unit
) {
    phones.forEachIndexed { index, phone ->
        Text(
            text = phone,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onClick(phone) }
        )

        if (index != phones.lastIndex) {
            Spacer(Modifier.height(Space8))
        }
    }
}
