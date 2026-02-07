package ru.practicum.android.diploma.data.mappers

import ru.practicum.android.diploma.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.domain.models.FilterIndustry

class FilterIndustriesMapper {
    fun toDomain(dto: FilterIndustryDto): FilterIndustry {
        return FilterIndustry(
            id = dto.id,
            name = dto.name
        )
    }

    fun toDomain(dtoList: List<FilterIndustryDto>): List<FilterIndustry> {
        return dtoList.map { dto ->
            toDomain(dto)
        }
    }
}
