@file:Suppress("ktlint:standard:filename")

package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.særbidrag.Særbidragskategori
import no.nav.bidrag.domene.tid.Datoperiode
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

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
    val type: String,
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

data class SærbidragsperiodeGrunnlag(
    @Schema(
        description =
            "Perioden særbidraget gjelder for. Er vanligvis lik starten og slutten av måneden på vedtakstidspunktet." +
                " Hvis vedtaket gjelder en klage er det vedtakstidspunktet på originale vedtaket",
    )
    val periode: Datoperiode = Datoperiode(YearMonth.now().atDay(1), YearMonth.now().atEndOfMonth()),
) : GrunnlagInnhold {
    constructor(virkningstidspunkt: YearMonth) : this(Datoperiode(virkningstidspunkt.atDay(1), virkningstidspunkt.atEndOfMonth()))
    constructor(virkningstidspunkt: LocalDate) : this(YearMonth.from(virkningstidspunkt))
}
