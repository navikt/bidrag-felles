package no.nav.bidrag.domene.util

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun BigDecimal.årsbeløpTilMåndesbeløp() = divide(BigDecimal(12), MathContext(10, RoundingMode.HALF_UP))

fun BigDecimal.avrundet(antallDesimaler: Int) = setScale(antallDesimaler, RoundingMode.HALF_UP)

val BigDecimal.avrundetTilNærmesteTier get() = divide(BigDecimal.TEN, 0, RoundingMode.HALF_UP).multiply(BigDecimal.TEN)
val BigDecimal.avrundetTilTiDesimaler get() = avrundet(10)
val BigDecimal.avrundetTilToDesimaler get() = avrundet(2)
val BigDecimal.avrundetTilNullDesimaler get() = avrundet(0)
