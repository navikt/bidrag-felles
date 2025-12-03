package no.nav.bidrag.generer.testdata.sak

import java.time.Year
import kotlin.random.Random

fun genererSaksnummer(year: Year? = null): String {
    val yearToUse = year ?: Year.of(Random.nextInt(2010, Year.now().value + 1))
    val yearPrefix = yearToUse.value % 100
    val randomSuffix = Random.nextInt(1, 100000) // 1 to 99999
    val saksnummer = yearPrefix * 100000 + randomSuffix
    return saksnummer.toString().padStart(7, '0')
}