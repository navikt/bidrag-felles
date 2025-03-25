package no.nav.bidrag.transport.behandling.vedtak.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import no.nav.bidrag.domene.enums.vedtak.Beslutningstype
import no.nav.bidrag.domene.enums.vedtak.Innkrevingstype
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.enums.vedtak.Vedtakskilde
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.organisasjon.Enhetsnummer
import no.nav.bidrag.domene.sak.Saksnummer
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
    @Schema(description = "Referanse som er unik for vedtaket")
    val unikReferanse: String? = null,
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
    @Schema(description = "Liste over alle stønader og deres antatt siste vedtak")
    @field:Valid
    val stønadSisteVedtakListe: List<StønadSisteVedtak> = emptyList(),
)

@Schema
data class StønadSisteVedtak(
    @Schema(description = "Stønadstype")
    @NotBlank
    val type: Stønadstype,
    @Schema(description = "Referanse til sak")
    val sak: Saksnummer,
    @Schema(description = "Personidenten til den som skal betale bidraget")
    val skyldner: Personident,
    @Schema(description = "Personidenten til den som krever bidraget")
    val kravhaver: Personident,
    @Schema(description = "Personidenten til den som mottar bidraget")
    val mottaker: Personident,
    @Schema(description = "Unik id for antatt siste vedtak")
    val sisteVedtaksid: Long,
)