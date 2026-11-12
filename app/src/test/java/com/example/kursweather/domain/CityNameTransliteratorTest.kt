package com.example.kursweather.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class CityNameTransliteratorTest {

    @Test
    fun cyrillicNames_becomeLatin() {
        assertEquals("Izhevsk", CityNameTransliterator.toLatin("Ижевск"))
        assertEquals("Votkinsk", CityNameTransliterator.toLatin("Воткинск"))
        assertEquals("Nizhniy Novgorod", CityNameTransliterator.toLatin("Нижний Новгород"))
        assertEquals("Chelyabinsk", CityNameTransliterator.toLatin("Челябинск"))
    }

    @Test
    fun latinNames_stayAsTyped() {
        assertEquals("Kazan", CityNameTransliterator.toLatin("Kazan"))
        assertEquals("Saint Petersburg", CityNameTransliterator.toLatin("Saint Petersburg"))
    }
}
