package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.FilterAreaDto
import ru.practicum.android.diploma.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.dto.VacancyResponseDto

open class Response(val resultCode: Int)
class AreasResponse(val data: List<FilterAreaDto>, code: Int = 200) : Response(code)
class IndustriesResponse(val data: List<FilterIndustryDto>, code: Int = 200) : Response(code)
class VacanciesResponse(val data: VacancyResponseDto, code: Int = 200) : Response(code)
class VacancyDetailsResponse(val data: VacancyDetailDto, code: Int = 200) : Response(code)
