package ru.practicum.android.diploma.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VacancyDetailUi(
    val name: String,
    val description: String?,
    val salary: String,
    val address: String?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val contactsName: String?,
    val contactsEmail: String?,
    val contactsPhone: List<String>?,
    val employerName: String,
    val employerLogoLink: String?,
    val skills: List<String>,
    val url: String,
    val industry: String
) : Parcelable
