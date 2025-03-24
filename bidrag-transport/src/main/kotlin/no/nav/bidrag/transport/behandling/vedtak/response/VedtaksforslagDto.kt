package no.nav.bidrag.transport.behandling.vedtak.response

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.vedtak.Vedtakskilde
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.organisasjon.Enhetsnummer
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import java.time.LocalDate
import java.time.LocalDateTime

@Schema
data class VedtaksforslagDto(
    @Schema(description = "Hva er kilden til vedtakforslaget. Automatisk eller manuelt")
    val kilde: Vedtakskilde,
    @Schema(description = "Type vedtakforslag")
    val type: Vedtakstype,
    @Schema(description = "Id til saksbehandler eller batchjobb som opprettet vedtakforslaget. For saksbehandler er ident hentet fra token")
    val opprettetAv: String,
    @Schema(description = "Saksbehandlers navn")
    val opprettetAvNavn: String?,
    @Schema(description = "Navn på applikasjon som vedtakforslaget er opprettet i")
    val kildeapplikasjon: String,
    @Schema(description = "Alltid null for vedtakforslag")
    val vedtakstidspunkt: LocalDateTime?,
    @Schema(description = "Enheten som er ansvarlig for vedtakforslaget. Kan være null for feks batch")
    val enhetsnummer: Enhetsnummer?,
    @Schema(description = "Settes hvis overføring til Elin skal utsettes")
    val innkrevingUtsattTilDato: LocalDate?,
    @Schema(description = "Settes hvis vedtaket er fastsatt i utlandet")
    val fastsattILand: String?,
    @Schema(description = "Tidspunkt vedtaket er fattet")
    val opprettetTidspunkt: LocalDateTime,
    @Schema(description = "Liste over alle grunnlag som inngår i vedtaket")
    val grunnlagListe: List<GrunnlagDto>,
    @Schema(description = "Liste over alle stønadsendringer som inngår i vedtaket")
    val stønadsendringListe: List<StønadsendringDto>,
    @Schema(description = "Liste over alle engangsbeløp som inngår i vedtaket")
    val engangsbeløpListe: List<EngangsbeløpDto>,
    @Schema(description = "Liste med referanser til alle behandlinger som ligger som grunnlag til vedtaket")
    val behandlingsreferanseListe: List<BehandlingsreferanseDto>,
)
