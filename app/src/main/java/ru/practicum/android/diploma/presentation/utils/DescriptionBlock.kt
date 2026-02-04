package ru.practicum.android.diploma.presentation.utils

sealed interface DescriptionBlock {
    data class Heading(val text: String) : DescriptionBlock
    data class Paragraph(val text: String) : DescriptionBlock
    data class Bullets(val items: List<String>) : DescriptionBlock
}
