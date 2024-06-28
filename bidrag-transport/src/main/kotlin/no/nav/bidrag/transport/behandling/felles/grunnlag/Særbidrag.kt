@file:Suppress("ktlint:standard:filename")

package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.særbidrag.Særbidragskategori
import no.nav.bidrag.domene.enums.særbidrag.Utgiftstype
import java.math.BigDecimal
import java.time.LocalDate

data class SærbidragskategoriGrunnlag(
    val kategori: Særbidragskategori,
    @get:Schema(
        description =
            "Beskrivelse på kategorien. Er påkrevd hvis kategori er ANNET",
    )
    val beskrivelse: String? = null,
) : GrunnlagInnhold

data class UtgiftspostGrunnlag(
    val dato: LocalDate,
    val type: Utgiftstype,
    val kravbeløp: BigDecimal,
    val godkjentBeløp: BigDecimal,
    @Schema(description = "Begrunnelse på hvorfor godkjent beløp avviker fra kravbeløp. Er påkrevd hvis kravbeløp er ulik godkjent beløp")
    val begrunnelse: String? = null,
    val betaltAvBp: Boolean = false,
) : GrunnlagInnhold

data class UtgiftDirekteBetaltGrunnlag(
    @Schema(description = "Beløp som er overført direkte til BM. Kan være 0 eller høyere")
    val beløpDirekteBetalt: BigDecimal,
) : GrunnlagInnhold
