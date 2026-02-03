package ru.practicum.android.diploma.data.mappers

import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.VacancyDetail

class VacancyDetailEntityMapper {

    fun toEntity(model: VacancyDetail): VacancyDetailEntity {
        return VacancyDetailEntity(
            remoteId = model.id,
            name = model.name,
            city = model.address?.city.orEmpty(),
            industry = model.industry.name,
            salaryFrom = model.salary?.from,
            currency = model.salary?.currency,
            salaryTo = model.salary?.to,
            employer = model.employer.name,
            logo = model.employer.logo,
        )
    }

    fun toDomain(entity: VacancyDetailEntity): VacancyDetail {
        return VacancyDetail(
            id = entity.remoteId,
            isFavorite = true,
            name = entity.name?.trim().orEmpty(),
            description = null,
            salary = VacancyDetail.Salary(from = entity.salaryFrom, to = entity.salaryTo, currency = entity.currency),
            address = VacancyDetail.Address(
                city = entity.city.orEmpty(),
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
            industry = FilterIndustry(
                id = 0,
                name = entity.industry?.trim().orEmpty()
            )
        )
    }
}
