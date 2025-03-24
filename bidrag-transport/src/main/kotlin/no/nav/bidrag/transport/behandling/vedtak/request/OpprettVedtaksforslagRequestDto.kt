package no.nav.bidrag.transport.behandling.vedtak.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import no.nav.bidrag.domene.enums.vedtak.Vedtakskilde
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.organisasjon.Enhetsnummer
import java.time.LocalDate
import java.time.LocalDateTime

@Schema
data class OpprettVedtaksforslagRequestDto(
    @Schema(description = "Hva er kilden til vedtaket. Automatisk eller manuelt")
    val kilde: Vedtakskilde,
    @Schema(description = "Type vedtak")
    val type: Vedtakstype,
    @Schema(description = "Skal bare brukes ved batchkjøring. Id til batchjobb som oppretter vedtaket")
    val opprettetAv: String? = null,
    @Schema(description = "Settes til null for nye vedtaksforslag")
    val vedtakstidspunkt: LocalDateTime? = null,
    @Schema(description = "Enheten som er ansvarlig for vedtaket. Kan være null for feks batch")
    @NotBlank
    val enhetsnummer: Enhetsnummer? = null,
    @Schema(description = "Settes hvis overføring til Elin skal utsettes")
    val innkrevingUtsattTilDato: LocalDate? = null,
    @Schema(description = "Settes hvis vedtaket er fastsatt i utlandet")
    val fastsattILand: String? = null,
    @Schema(description = "Liste over alle grunnlag som inngår i vedtaket")
    @field:Valid
    val grunnlagListe: List<OpprettGrunnlagRequestDto>,
    @Schema(description = "Liste over alle stønadsendringer som inngår i vedtaket")
    @field:Valid
    val stønadsendringListe: List<OpprettStønadsendringRequestDto> = emptyList(),
    @Schema(description = "Liste over alle engangsbeløp som inngår i vedtaket")
    @field:Valid
    val engangsbeløpListe: List<OpprettEngangsbeløpRequestDto> = emptyList(),
    @Schema(description = "Liste med referanser til alle behandlinger som ligger som grunnlag til vedtaket")
    @field:Valid
    val behandlingsreferanseListe: List<OpprettBehandlingsreferanseRequestDto> = emptyList(),
)
