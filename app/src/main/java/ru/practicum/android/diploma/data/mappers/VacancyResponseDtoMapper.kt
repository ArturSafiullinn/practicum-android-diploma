package ru.practicum.android.diploma.data.mappers

import ru.practicum.android.diploma.data.dto.VacancyResponseDto
import ru.practicum.android.diploma.domain.models.VacancyResponse

class VacancyResponseDtoMapper(
    private val vacancyDetailMapper: VacancyDetailDtoMapper
) {
    fun toDomain(dto: VacancyResponseDto): VacancyResponse {
        return VacancyResponse(
            found = dto.found,
            pages = dto.pages,
            page = dto.page,
            vacancies = (dto.items ?: emptyList()).map { vacancyDetailMapper.toDomain(it) }
        )
    }
}
