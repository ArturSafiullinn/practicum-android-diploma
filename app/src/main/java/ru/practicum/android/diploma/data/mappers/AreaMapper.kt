package ru.practicum.android.diploma.data.mappers

import ru.practicum.android.diploma.data.dto.FilterAreaDto
import ru.practicum.android.diploma.domain.models.Area

fun FilterAreaDto.toFlatList(): List<Area> {
    val result = mutableListOf<Area>()

    result.add(
        Area(
            id = id,
            name = name,
            parentId = parentId
        )
    )

    areas.forEach { child ->
        result.addAll(child.toFlatList())
    }

    return result
}
