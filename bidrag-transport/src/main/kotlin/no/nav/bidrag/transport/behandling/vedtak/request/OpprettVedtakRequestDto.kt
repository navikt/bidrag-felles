package no.nav.bidrag.transport.behandling.vedtak.request

import com.fasterxml.jackson.databind.JsonNode
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.enums.vedtak.BehandlingsrefKilde
import no.nav.bidrag.domene.enums.vedtak.Beslutningstype
import no.nav.bidrag.domene.enums.vedtak.Engangsbeløptype
import no.nav.bidrag.domene.enums.vedtak.Innkrevingstype
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.enums.vedtak.Vedtakskilde
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.organisasjon.Enhetsnummer
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Schema
data class OpprettVedtakRequestDto(

    @Schema(description = "Hva er kilden til vedtaket. Automatisk eller manuelt")
    val kilde: Vedtakskilde,

    @Schema(description = "Type vedtak")
    val type: Vedtakstype,

    @Schema(description = "Skal bare brukes ved batchkjøring. Id til batchjobb som oppretter vedtaket")
    val opprettetAv: String?,

    @Schema(description = "Tidspunkt/timestamp når vedtaket er fattet")
    val vedtakstidspunkt: LocalDateTime,

    @Schema(description = "Enheten som er ansvarlig for vedtaket. Kan være null for feks batch")
    @NotBlank
    val enhetsnummer: Enhetsnummer?,

    @Schema(description = "Settes hvis overføring til Elin skal utsettes")
    val innkrevingUtsattTilDato: LocalDate?,

    @Schema(description = "Settes hvis vedtaket er fastsatt i utlandet")
    val fastsattILand: String?,

    @Schema(description = "Liste over alle grunnlag som inngår i vedtaket")
    @field:Valid
    val grunnlagListe: List<OpprettGrunnlagRequestDto>,

    @Schema(description = "Liste over alle stønadsendringer som inngår i vedtaket")
    @field:Valid
    val stønadsendringListe: List<OpprettStønadsendringRequestDto>?,

    @Schema(description = "Liste over alle engangsbeløp som inngår i vedtaket")
    @field:Valid
    val engangsbeløpListe: List<OpprettEngangsbeløpRequestDto>?,

    @Schema(description = "Liste med referanser til alle behandlinger som ligger som grunnlag til vedtaket")
    @field:Valid
    val behandlingsreferanseListe: List<OpprettBehandlingsreferanseRequestDto>?,
)

@Schema
data class OpprettGrunnlagRequestDto(

    @Schema(description = "Referanse til grunnlaget")
    @NotBlank
    val referanse: String,

    @Schema(description = "Grunnlagstype")
    @NotBlank
    val type: Grunnlagstype,

    @Schema(description = "Innholdet i grunnlaget")
    @NotBlank
    val innhold: JsonNode,
)

@Schema
data class OpprettStønadsendringRequestDto(

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

    @Schema(description = "Angir første år en stønad skal indeksreguleres")
    val førsteIndeksreguleringsår: Int?,

    @Schema(description = "Angir om stønaden skal innkreves")
    val innkreving: Innkrevingstype,

    @Schema(
        description = "Angir om søknaden om stønadsendring er besluttet avvist, stadfestet eller skal medføre endring" +
            "Gyldige verdier er 'AVVIST', 'STADFESTELSE' og 'ENDRING'",
    )
    val beslutning: Beslutningstype,

    @Schema(description = "Id for vedtaket det er klaget på")
    val omgjørVedtakId: Int?,

    @Schema(description = "Referanse som brukes i utlandssaker")
    val eksternReferanse: String?,

    @Schema(description = "Liste over grunnlag som er knyttet direkte til stønadsendringen")
    val grunnlagReferanseListe: List<String>,

    @Schema(description = "Liste over alle perioder som inngår i stønadsendringen")
    @field:Valid
    val periodeListe: List<OpprettPeriodeRequestDto>,
)

@Schema
data class OpprettPeriodeRequestDto(

    @Schema(description = "Periode med fra-og-med-dato og til-dato med format ÅÅÅÅ-MM")
    val periode: ÅrMånedsperiode,

    @Schema(description = "Beregnet stønadsbeløp")
    @Min(0)
    val beløp: BigDecimal?,

    @Schema(description = "Valutakoden tilhørende stønadsbeløpet")
    @NotBlank
    val valutakode: String?,

    @Schema(description = "Resultatkoden tilhørende stønadsbeløpet")
    @NotBlank
    val resultatkode: String,

    @Schema(description = "Referanse - delytelseId/beslutningslinjeId -> bidrag-regnskap. Skal fjernes senere")
    val delytelseId: String?,

    @Schema(description = "Liste over alle grunnlag som inngår i perioden")
    @NotEmpty
    val grunnlagReferanseListe: List<String>,
)

@Schema
data class OpprettEngangsbeløpRequestDto(

    @Schema(description = "Beløpstype. Saertilskudd, gebyr m.m.")
    @NotBlank
    val type: Engangsbeløptype,

    @Schema(description = "Referanse til sak")
    val sak: Saksnummer,

    @Schema(description = "Personidenten til den som skal betale engangsbeløpet")
    val skyldner: Personident,

    @Schema(description = "Personidenten til den som krever engangsbeløpet")
    val kravhaver: Personident,

    @Schema(description = "Personidenten til den som mottar engangsbeløpet")
    val mottaker: Personident,

    @Schema(description = "Beregnet engangsbeløp")
    @Min(0)
    val beløp: BigDecimal?,

    @Schema(description = "Valutakoden tilhørende engangsbeløpet")
    @NotBlank
    val valutakode: String?,

    @Schema(description = "Resultatkoden tilhørende engangsbeløpet")
    @NotBlank
    val resultatkode: String,

    @Schema(description = "Angir om engangsbeløpet skal innkreves")
    val innkreving: Innkrevingstype,

    @Schema(
        description = "Angir om søknaden om engangsbeløp er besluttet avvist, stadfestet eller skal medføre endring" +
            "Gyldige verdier er 'AVVIST', 'STADFESTELSE' og 'ENDRING'",
    )
    val beslutning: Beslutningstype,

    @Schema(description = "Id for vedtaket det er klaget på. Utgjør sammen med referanse en unik id for et engangsbeløp")
    val omgjørVedtakId: Int?,

    @Schema(
        description = "Referanse til engangsbeløp, brukes for å kunne omgjøre engangsbeløp senere i et klagevedtak. Unik innenfor et vedtak. " +
            "Unik referanse blir generert av bidrag-vedtak hvis den ikke er angitt i requesten.",
    )
    val referanse: String?,

    @Schema(description = "Referanse - delytelsesId/beslutningslinjeId -> bidrag-regnskap. Skal fjernes senere")
    val delytelseId: String?,

    @Schema(description = "Referanse som brukes i utlandssaker")
    val eksternReferanse: String?,

    @Schema(description = "Liste over alle grunnlag som inngår i engangsbeløpet")
    @NotEmpty
    val grunnlagReferanseListe: List<String>,
)

@Schema
data class OpprettBehandlingsreferanseRequestDto(

    @Schema(description = "Kilde/type for en behandlingsreferanse")
    @NotBlank
    val kilde: BehandlingsrefKilde,

    @Schema(description = "Kildesystemets referanse til behandlingen")
    @NotBlank
    val referanse: String,
)
