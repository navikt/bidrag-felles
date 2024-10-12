package no.nav.bidrag.domene.util

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun BigDecimal.årsbeløpTilMåndesbeløp() = divide(BigDecimal(12), MathContext(10, RoundingMode.HALF_UP))
