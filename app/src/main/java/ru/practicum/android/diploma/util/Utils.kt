package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.ui.models.VacancyDetailUi

fun formatSalarySpaced(amount: Int?): String {
    return amount?.let {
        "%,d".format(it).replace(',', ' ')
    } ?: ""
}

// todo: Удалить, когда добавим переходы по реальным вакансиям
fun getMockVacancy(): VacancyDetailUi = VacancyDetailUi(
    id = "1",
    isFavorite = true,
    name = "Test vacancy",
    description = "Задачи, которые могут стать твоими:\n\nРазработка новой функциональности мобильного приложения под Android, его архитектуры и исправление существующих недостатков;\nНаписание качественного, чистого, читаемого кода, code-review;\nРазработка общих архитектурных решений;\nВзаимодействие с менеджерами, дизайнерами, бекендерами, тестировщиками;\nПроактивно участвовать в жизни продукта: обсуждении требований, планировании проектов, проектировании дизайна, прототипов, спецификаций;\nДелиться технической экспертизой: предлагать, обсуждать и интегрировать новые решения;\nДекомпозировать, оценивать сроки реализации задач и выдерживать их;\nПроектировать клиент-серверное взаимодействие;\nРазбираться в чужом коде и проводить его рефакторинг;\nДоносить свои мысли и отстаивать свою точку зрения перед остальными членами команды;\nНе просто накидывать идеи, а реализовывать и доводить их до конца в общем проекте;\n\nЧто нужно знать:\n\nAndroid SDK, Android Support Libraries\nПаттерны построения мобильного UI/UX, принципы Material Desig\nПаттерны проектирования, ООП, SOLID, понимание функционального реактивного кода, Clean; Architecture\nKotlin\nDagger, Kotlin Coroutines, Kotlin Flow, Compose, MVVM / MVI, Room\nGradle Multi Modules\nЗнание архитектуры OS Android и особенностей его версий 21+\nВладение техническим английским языком на уровне чтения и понимания\n\nБудет плюсом:\n\nОпыт RxJava\nОпыт написания Unit и UI тестов\nОпыт в Backend Driven UI подходе\nРабота с Gradle\nОпыт работы с CI&DI",
    salary = "От 100 000 Р",
    city = "Москва",
    fullAddress = "г. Москва, Красная пл., д.1",
    experience = "Не важен",
    schedule = "5/2",
    employment = "",
    contactsName = "Contacts Name",
    contactsEmail = "Contacts email",
    contactsPhone = listOf("contacts phone", "8-800-555-3535"),
    employerName = "Yandex",
    employerLogoLink = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f1/Yandex_logo_2021_Russian.svg/1024px-Yandex_logo_2021_Russian.svg.png",
    area = "Москва",
    skills = listOf("Программирование", "компутеры"),
    url = "yandex.ru",
    industry = "IT"
)
