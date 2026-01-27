package ru.practicum.android.diploma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(

    // 1) Заголовки фрагментов, название вакансии в списке, тексты заглушек
    titleLarge = TextStyle(
        fontFamily = YsDisplay,
        fontWeight = FontWeight.Medium, // 500
        fontSize = 22.sp,
        lineHeight = 26.sp
    ),

    // 2) Основной текст (описания, подсказка в поле, введенный текст и т.п.)
    bodyLarge = TextStyle(
        fontFamily = YsDisplay,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 16.sp,
        lineHeight = 19.sp
    ),

    // 3) Основной текст жирный (кнопки, список разработчиков)
    bodyMedium = TextStyle(
        fontFamily = YsDisplay,
        fontWeight = FontWeight.Medium, // 500
        fontSize = 16.sp,
        lineHeight = 19.sp
    ),

    // 4) Navigation View, верхние подсказки над полем ввода и т.п.
    labelMedium = TextStyle(
        fontFamily = YsDisplay,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),

    // 5) Большой заголовок (О вакансии / Над приложением работали)
    displaySmall = TextStyle(
        fontFamily = YsDisplay,
        fontWeight = FontWeight.Bold, // 700
        fontSize = 32.sp,
        lineHeight = 38.sp
    )
)
