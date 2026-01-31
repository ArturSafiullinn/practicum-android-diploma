package ru.practicum.android.diploma.ui.screens.vacancy

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import ru.practicum.android.diploma.ui.models.VacancyDetailUi
import ru.practicum.android.diploma.ui.theme.Dimens.IconContainer
import ru.practicum.android.diploma.ui.theme.Dimens.Radius12
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space8
import ru.practicum.android.diploma.ui.theme.Gray100
import ru.practicum.android.diploma.util.DrawableRes

@Composable
fun VacancyBanner(vacancy: VacancyDetailUi) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(
                color = Gray100,
                RoundedCornerShape(Radius12)
            )
            .padding(Space16)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier
                    .size(IconContainer, IconContainer)
                    .clip(RoundedCornerShape(Radius12)),
                model = vacancy.employerLogoLink,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                placeholder = painterResource(DrawableRes.logoPlaceholder),
                error = painterResource(DrawableRes.logoPlaceholder),
                onLoading = {
                    Log.d("CoilDebug", "Загрузка началась: ${vacancy.employerLogoLink}")
                },
                onSuccess = { state ->
                    Log.d("CoilDebug", "Картинка загружена. Размер: ${state.painter.intrinsicSize}")
                },
                onError = { state ->
                    Log.e("CoilDebug", "Ошибка загрузки изображения")
                    state.result.throwable.printStackTrace()
                }
            )

            Spacer(Modifier.width(Space8))

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = vacancy.employerName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = vacancy.area,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
