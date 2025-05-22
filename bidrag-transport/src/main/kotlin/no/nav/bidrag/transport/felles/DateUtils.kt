package no.nav.bidrag.transport.felles

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

val formatterCompact = DateTimeFormatter.ofPattern("yyyyMMdd")
val formatterVisningsnavn = DateTimeFormatter.ofPattern("dd.MM.yyyy")
val formatterCompactLocalDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
val formatterCompactYearMonth = DateTimeFormatter.ofPattern("yyyyMM")

fun LocalDate?.tilVisningsnavn(): String = this?.format(formatterVisningsnavn) ?: ""

fun LocalDate?.toCompactString(): String = this?.format(formatterCompact) ?: ""

fun LocalDateTime?.toCompactString(): String = this?.format(formatterCompactLocalDateTime) ?: ""

fun YearMonth?.toCompactString(): String = this?.format(formatterCompactYearMonth) ?: ""
