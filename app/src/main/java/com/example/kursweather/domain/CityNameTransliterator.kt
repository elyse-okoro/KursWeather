package com.example.kursweather.domain

/**
 * API Ninjas ищет города по латинскому названию. Если пользователь набрал название
 * кириллицей («Ижевск»), переводим его в латиницу («Izhevsk»).
 */
object CityNameTransliterator {

    private val letters = mapOf(
        'а' to "a", 'б' to "b", 'в' to "v", 'г' to "g", 'д' to "d", 'е' to "e", 'ё' to "e",
        'ж' to "zh", 'з' to "z", 'и' to "i", 'й' to "y", 'к' to "k", 'л' to "l", 'м' to "m",
        'н' to "n", 'о' to "o", 'п' to "p", 'р' to "r", 'с' to "s", 'т' to "t", 'у' to "u",
        'ф' to "f", 'х' to "kh", 'ц' to "ts", 'ч' to "ch", 'ш' to "sh", 'щ' to "shch",
        'ъ' to "", 'ы' to "y", 'ь' to "", 'э' to "e", 'ю' to "yu", 'я' to "ya"
    )

    fun hasCyrillic(text: String): Boolean = text.any { it.lowercaseChar() in letters }

    fun toLatin(text: String): String = buildString {
        for (char in text) {
            val latin = letters[char.lowercaseChar()]
            when {
                latin == null -> append(char)
                char.isUpperCase() -> append(latin.replaceFirstChar { it.uppercaseChar() })
                else -> append(latin)
            }
        }
    }
}
