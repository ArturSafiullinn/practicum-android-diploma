package ru.practicum.android.diploma.ui.screens.filtr

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Dimens
import ru.practicum.android.diploma.ui.theme.Gray400
import ru.practicum.android.diploma.ui.theme.Red
import ru.practicum.android.diploma.ui.theme.White

@Composable
fun ExpectedSalaryField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = value,
        onValueChange = { handleSalaryInput(it, onValueChange) },
        singleLine = true,

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done // 👈 кнопка "Готово / Применить"
        ),

        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus() // 👈 фокус ушёл, клавиатура закрылась
            }
        ),

        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.SalaryImputHeihgt)
            .onFocusChanged { isFocused = it.isFocused },
        decorationBox = { innerTextField ->
            SalaryDecoration(
                value = value,
                isFocused = isFocused,
                onClear = onClear,
                innerTextField = innerTextField
            )
        }
    )
}

private fun handleSalaryInput(
    newText: String,
    onValueChange: (String) -> Unit
) {
    if (newText.isEmpty() || newText.all(Char::isDigit)) {
        onValueChange(newText)
    }
}

@Composable
private fun SalaryDecoration(
    value: String,
    isFocused: Boolean,
    onClear: () -> Unit,
    innerTextField: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(Dimens.Radius12)
            )
            .padding(horizontal = Dimens.Space16, vertical = Dimens.Space8)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SalaryTextContent(
                value = value,
                isFocused = isFocused,
                innerTextField = innerTextField,
                modifier = Modifier.weight(1f)
            )

            SalaryClearIcon(
                visible = isFocused && value.isNotEmpty(),
                onClick = onClear
            )
        }
    }
}

@Composable
private fun SalaryTextContent(
    value: String,
    isFocused: Boolean,
    innerTextField: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SalaryLabel(isFocused, value)

        Box {
            if (value.isEmpty() && !isFocused) {
                Text(
                    text = stringResource(R.string.salary_placeholder),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
            }
            innerTextField()
        }
    }
}

@Composable
private fun SalaryLabel(
    isFocused: Boolean,
    value: String
) {
    Text(
        text = stringResource(R.string.salary_label),
        style = MaterialTheme.typography.labelMedium,
        color = if (isFocused) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )
}

@Composable
private fun SalaryClearIcon(
    visible: Boolean,
    onClick: () -> Unit
) {
    if (!visible) {
        return
    }

    Icon(
        painter = painterResource(R.drawable.ic_clear),
        contentDescription = null,
        modifier = Modifier
            .size(Dimens.SmallIconSize)
            .clickable(onClick = onClick),
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun FilterClickableField(
    label: String,
    value: String?,
    placeholder: String = "Не выбрано",
    onClick: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ListItemHeight)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (value.isNullOrEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray400
                    )
                } else {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Icon(
                painter = painterResource(
                    if (value.isNullOrEmpty())
                        R.drawable.ic_arrow_forward
                    else
                        R.drawable.ic_clear
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(Dimens.SmallIconSize)
                    .clickable(
                        enabled = !value.isNullOrEmpty(),
                        onClick = onClear
                    ),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SalaryFilterItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.no_salary_checkbox),
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.primary,
                checkmarkColor = MaterialTheme.colorScheme.background
            )
        )

    }
}

@Composable
fun ApplyButton(
    text: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonText = text ?: stringResource(R.string.apply_button_text)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.BottomApplyHeight)
            .background(
                color = Blue,
                shape = RoundedCornerShape(Dimens.Radius12)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buttonText,
            color = White,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ResetButton(
    text: String = stringResource(R.string.cancel_button_text),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.BottomApplyHeight)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Red,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}
