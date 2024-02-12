package no.nav.bidrag.transport.felles

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

val formatterCompact = DateTimeFormatter.ofPattern("yyyyMMdd")
val formatterCompactYearMonth = DateTimeFormatter.ofPattern("yyyyMM")

fun LocalDate?.toCompactString(): String = this?.format(formatterCompact) ?: ""

fun YearMonth?.toCompactString(): String = this?.format(formatterCompactYearMonth) ?: ""
