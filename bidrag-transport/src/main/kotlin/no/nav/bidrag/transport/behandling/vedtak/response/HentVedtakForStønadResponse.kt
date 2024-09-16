package no.nav.bidrag.transport.behandling.vedtak.response

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.vedtak.Beslutningstype
import no.nav.bidrag.domene.enums.vedtak.Innkrevingstype
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import no.nav.bidrag.transport.behandling.felles.grunnlag.Grunnlagsreferanse
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "Respons med alle endringsvedtak for en stønad (saksnr, stønadstype, skyldner, kravhaver")
data class HentVedtakForStønadResponse(
    @Schema(description = "Liste med vedtak for stønad")
    val vedtakListe: List<VedtakForStønad> = emptyList(),
)

@Schema(description = "Objekt med relevant informasjon fra vedtak")
data class VedtakForStønad(
    @Schema(description = "Unik id generert for vedtak")
    val vedtaksid: Int,
    @Schema(description = "Dato vedtaket er fattet")
    val vedtaksdato: LocalDate,
    @Schema(description = "Type vedtak")
    val type: Vedtakstype,
    @Schema(description = "Ident til søknaden vedtaket er fattet for")
    val søknadsid: String?,
    @Schema(description = "Stønadsendring")
    val stønadsendring: StønadsendringBidrag,
    @Schema(description = "Liste over alle grunnlag som inngår i vedtaket. Listen vil være tom til grunnlagsoverføring er gjort")
    val grunnlagListe: List<GrunnlagDto> = emptyList(),
)

@Schema(description = "Relevant informasjon om stønadsendringer i et vedtak")
data class StønadsendringBidrag(
    @Schema(description = "Saksnummer")
    val sak: Saksnummer,
    @Schema(description = "Stønadstype")
    val type: Stønadstype,
    @Schema(description = "Personidenten til den som skal betale bidraget")
    val skyldner: Personident,
    @Schema(description = "Personidenten til den som krever bidraget")
    val kravhaver: Personident,
    @Schema(description = "Angir om stønaden skal innkreves")
    val innkreving: Innkrevingstype = Innkrevingstype.MED_INNKREVING,
    @Schema(
        description =
            "Angir om søknaden om engangsbeløp er besluttet avvist, stadfestet eller skal medføre endring" +
                "Gyldige verdier er 'AVVIST', 'STADFESTELSE' og 'ENDRING'",
    )
    val beslutning: Beslutningstype = Beslutningstype.ENDRING,
    @Schema(description = "Id for vedtaket det er klaget på")
    val omgjørVedtakId: Int?,
    @Schema(description = "Liste over alle perioder som inngår i stønadsendringen")
    val periodeListe: List<Stønadsperiode>,
)

@Schema(description = "Perioder tilhørende en stønadsendring")
data class Stønadsperiode(
    @Schema(description = "Periode med fra-og-med-dato og til-dato med format ÅÅÅÅ-MM")
    val periode: ÅrMånedsperiode,
    @Schema(description = "Beregnet stønadsbeløp")
    val beløp: BigDecimal?,
    @Schema(description = "Valutakoden tilhørende stønadsbeløpet")
    val valutakode: String?,
    @Schema(description = "Resultatkoden tilhørende stønadsbeløpet")
    val resultatkode: String,
    @Schema(description = "Liste over alle grunnlag som inngår i perioden. Listen vil være tom til grunnlagsoverføring er gjort")
    val grunnlagReferanseListe: List<Grunnlagsreferanse> = emptyList(),
)
