package no.nav.bidrag.generer.testdata.sak

import java.time.Year
import kotlin.random.Random

fun genererSaksnummer(år: Year? = null): String {
    val år = år ?: Year.of(Random.nextInt(2010, Year.now().value + 1))
    val årPrefix = år.value % 100
    val tilfeldigSuffix = Random.nextInt(1, 100000)
    val saksnummer = årPrefix * 100000 + tilfeldigSuffix
    return saksnummer.toString().padStart(7, '0') // Padder i tilfelle året ikke gir 2 prefix siffer
}
