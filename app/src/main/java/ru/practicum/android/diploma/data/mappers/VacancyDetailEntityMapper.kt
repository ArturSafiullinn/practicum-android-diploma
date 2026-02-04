package ru.practicum.android.diploma.data.mappers

import com.google.gson.Gson
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.VacancyDetail

class VacancyDetailEntityMapper(
    private val gson: Gson
) {

    fun toEntity(model: VacancyDetail): VacancyDetailEntity =
        VacancyDetailEntity(
            remoteId = model.id,
            name = model.name,
            city = model.address?.city,
            employer = model.employer.name,
            logo = model.employer.logo,
            industry = model.industry.name,
            currency = model.salary?.currency,
            salaryFrom = model.salary?.from,
            salaryTo = model.salary?.to,
            detailsJson = gson.toJson(model)
        )

    fun toDomain(entity: VacancyDetailEntity): VacancyDetail {
        val json = entity.detailsJson

        if (!json.isNullOrBlank()) {
            runCatching {
                gson.fromJson(json, VacancyDetail::class.java)
            }.getOrNull()?.let { restored ->
                return restored.copy(isFavorite = true)
            }
        }

        return VacancyDetail(
            id = entity.remoteId,
            isFavorite = true,
            name = entity.name?.trim().orEmpty(),
            description = null,
            salary = VacancyDetail.Salary(
                from = entity.salaryFrom,
                to = entity.salaryTo,
                currency = entity.currency
            ),
            address = VacancyDetail.Address(
                city = entity.city,
                street = null,
                building = null,
                fullAddress = null
            ),
            experience = null,
            schedule = null,
            employment = null,
            contacts = null,
            employer = VacancyDetail.Employer(
                id = "",
                name = entity.employer.orEmpty(),
                logo = entity.logo
            ),
            area = FilterArea(id = 0, name = "", parentId = null, areas = emptyList()),
            skills = emptyList(),
            url = "",
            industry = FilterIndustry(id = 0, name = entity.industry?.trim().orEmpty())
        )
    }
}
