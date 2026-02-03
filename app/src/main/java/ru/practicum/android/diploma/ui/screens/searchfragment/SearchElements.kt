package ru.practicum.android.diploma.ui.screens.searchfragment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.models.VacancyListItemUi
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Dimens
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Gray100
import ru.practicum.android.diploma.ui.theme.White

@Composable
fun SearchInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        cursorBrush = SolidColor(Blue),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.SearchFieldHeight),

        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(Dimens.Radius12)
                    )
                    .padding(horizontal = Dimens.Space12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = Dimens.Space8)
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.enter_your_query),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    }
                    innerTextField()
                }

                Spacer(modifier = Modifier.width(Dimens.Space8))

                if (query.isEmpty()) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.SmallIconSize),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_clear),
                        contentDescription = null,
                        modifier = Modifier
                            .size(Dimens.SmallIconSize)
                            .clickable { onClearQuery() },
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
}

@Composable
fun VacancyItem(
    vacancy: VacancyListItemUi,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = Dimens.Space12),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.IconContainer)
                .border(
                    width = Dimens.Space1,
                    color = Gray100,
                    shape = RoundedCornerShape(Dimens.Radius12)
                )
                .padding(Dimens.Space8),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = vacancy.employerLogoUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.ic_placeholder_vacancy),
                error = painterResource(R.drawable.ic_placeholder_vacancy)
            )
        }

        Spacer(modifier = Modifier.width(Space16))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = vacancy.title,
                maxLines = 3,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(Dimens.Space4))
            Text(
                text = vacancy.employerName ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = vacancy.salary ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun VacancyCount(count: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.Space11, bottom = Dimens.Space12)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .background(
                color = Blue,
                shape = RoundedCornerShape(Dimens.Radius12)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.count_vacancy, count),
            style = MaterialTheme.typography.bodyLarge,
            color = White,
            modifier = Modifier.padding(horizontal = Dimens.Space12, vertical = Dimens.Space4)
        )
    }
}
