package ru.practicum.android.diploma.data.mappers

import ru.practicum.android.diploma.data.dto.FilterAreaDto
import ru.practicum.android.diploma.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.VacancyDetail

class VacancyDetailDtoMapper {

    fun toDomain(dto: VacancyDetailDto): VacancyDetail {
        return VacancyDetail(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            salary = salaryToDomain(dto.salary),
            address = addressToDomain(dto.address),
            experience = experienceToDomain(dto.experience),
            schedule = scheduleToDomain(dto.schedule),
            employment = employmentToDomain(dto.employment),
            contacts = contactsToDomain(dto.contacts),
            employer = employerToDomain(dto.employer),
            area = areaToDomain(dto.area),
            skills = dto.skills,
            url = dto.url,
            industry = industryToDomain(dto.industry)
        )
    }

    // region Функции конвертации внутренних полей VacancyDetail
    private fun salaryToDomain(dto: VacancyDetailDto.SalaryDto?): VacancyDetail.Salary? {
        return dto?.let {
            VacancyDetail.Salary(
                from = it.from,
                to = it.to,
                currency = it.currency
            )
        }
    }

    private fun addressToDomain(dto: VacancyDetailDto.AddressDto?): VacancyDetail.Address? {
        return dto?.let {
            VacancyDetail.Address(
                city = it.city,
                street = it.street,
                building = it.building,
                fullAddress = it.fullAddress
            )
        }
    }

    private fun experienceToDomain(dto: VacancyDetailDto.ExperienceDto?): VacancyDetail.Experience? {
        return dto?.let {
            VacancyDetail.Experience(
                id = it.id,
                name = it.name
            )
        }
    }

    private fun scheduleToDomain(dto: VacancyDetailDto.ScheduleDto?): VacancyDetail.Schedule? {
        return dto?.let {
            VacancyDetail.Schedule(
                id = it.id,
                name = it.name
            )
        }
    }

    private fun employmentToDomain(dto: VacancyDetailDto.EmploymentDto?): VacancyDetail.Employment? {
        return dto?.let {
            VacancyDetail.Employment(
                id = it.id,
                name = it.name
            )
        }
    }

    private fun contactsToDomain(dto: VacancyDetailDto.ContactsDto?): VacancyDetail.Contacts? {
        return dto?.let {
            VacancyDetail.Contacts(
                id = it.id,
                name = it.name,
                email = it.email,
                phone = it.phone
            )
        }
    }

    private fun employerToDomain(dto: VacancyDetailDto.EmployerDto): VacancyDetail.Employer {
        return VacancyDetail.Employer(
            id = dto.id,
            name = dto.name,
            logo = dto.logo
        )
    }

    private fun areaToDomain(dto: FilterAreaDto): FilterArea {
        return FilterArea(
            id = dto.id,
            name = dto.name,
            parentId = dto.parentId,
            areas = dto.areas.map { areaToDomain(it) }
        )
    }

    private fun industryToDomain(dto: FilterIndustryDto): FilterIndustry {
        return FilterIndustry(
            id = dto.id,
            name = dto.name
        )
    }
    // endregion
}
