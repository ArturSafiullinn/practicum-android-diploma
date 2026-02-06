package ru.practicum.android.diploma.data.mappers

import ru.practicum.android.diploma.data.dto.FilterAreaDto
import ru.practicum.android.diploma.domain.models.Area

fun FilterAreaDto.toFlatList(): List<Area> {
    val result = mutableListOf<Area>()

    if (parentId != null) {
        result.add(Area(id = id, name = name, parentId = parentId))
    }

    // рекурсивно добавляем все дочерние элементы
    areas.forEach { child ->
        result.addAll(child.toFlatList())
    }

    return result
}
