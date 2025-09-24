package no.nav.bidrag.transport.dokumentmaler

import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.beregning.Resultatkode.Companion.erDirekteAvslag
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.domene.util.visningsnavn
import no.nav.bidrag.domene.util.visningsnavnIntern
import no.nav.bidrag.domene.util.årsbeløpTilMåndesbeløp
import no.nav.bidrag.transport.behandling.felles.grunnlag.BeregnetBidragPerBarn
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBarnetilleggSkattesats
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBidragspliktigesAndel
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningEndringSjekkGrensePeriode
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningUnderholdskostnad
import no.nav.bidrag.transport.behandling.felles.grunnlag.ResultatFraVedtakGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.SluttberegningBarnebidrag
import no.nav.bidrag.transport.behandling.felles.grunnlag.SluttberegningBarnebidragAldersjustering
import no.nav.bidrag.transport.behandling.felles.grunnlag.SluttberegningIndeksregulering
import no.nav.bidrag.transport.behandling.vedtak.response.erIndeksEllerAldersjustering
import no.nav.bidrag.transport.dokumentmaler.notat.EndeligOrkestrertVedtak
import no.nav.bidrag.transport.dokumentmaler.notat.NotatMalType
import no.nav.bidrag.transport.dokumentmaler.notat.VedtakResultatInnhold
import no.nav.bidrag.transport.felles.tilVisningsnavn
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

data class DokumentmalManuellVedtak(
    val valgt: Boolean,
    val fattetTidspunkt: LocalDateTime,
    val virkningsDato: LocalDate,
    val vedtakstype: Vedtakstype,
    val resultatSistePeriode: String,
    val privatAvtale: Boolean,
    val begrensetRevurdering: Boolean,
) {
    val søknadstype get() =
        when {
            privatAvtale -> "Privat avtale"
            begrensetRevurdering -> "Begrenset revurdering"
            else -> vedtakstype.visningsnavn.intern
        }
}

data class DokumentmalResultatBidragsberegningBarnDto(
    val barn: DokumentmalPersonDto,
    val indeksår: Int? = null,
    val innkrevesFraDato: YearMonth? = null,
    val orkestrertVedtak: EndeligOrkestrertVedtak? = null,
    val perioder: List<ResultatBarnebidragsberegningPeriodeDto>,
) : VedtakResultatInnhold(NotatMalType.BIDRAG) {
    data class ResultatBarnebidragsberegningPeriodeDto(
        val periode: ÅrMånedsperiode,
        val underholdskostnad: BigDecimal = BigDecimal.ZERO,
        val bpsAndelU: BigDecimal = BigDecimal.ZERO,
        val bpsAndelBeløp: BigDecimal = BigDecimal.ZERO,
        val samværsfradrag: BigDecimal = BigDecimal.ZERO,
        val beregnetBidrag: BigDecimal = BigDecimal.ZERO,
        val faktiskBidrag: BigDecimal = BigDecimal.ZERO,
        val resultatKode: Resultatkode? = null,
        val erOpphør: Boolean = false,
        val erDirekteAvslag: Boolean = false,
        val vedtakstype: Vedtakstype,
        val beregningsdetaljer: BidragPeriodeBeregningsdetaljer? = null,
        val klageOmgjøringDetaljer: KlageOmgjøringDetaljer? = null,
        var delvedtakstypeVisningsnavn: String = "",
        var resultatkodeVisningsnavn: String = "",
        val resultatFraVedtak: ResultatFraVedtakGrunnlag? = null,
    ) {
        init {
            if (delvedtakstypeVisningsnavn.isEmpty()) {
                delvedtakstypeVisningsnavn = tilDelvedtakstypeVisningsnavn()
            }
            if (resultatkodeVisningsnavn.isEmpty()) {
                resultatkodeVisningsnavn = tilResultatkodeVisningsnavn()
            }
        }

        private val erKlagevedtak get() =
            resultatFraVedtak != null && resultatFraVedtak.omgjøringsvedtak &&
                vedtakstype == Vedtakstype.KLAGE
        private val erOmgjøringsvedtak get() =
            resultatFraVedtak != null && resultatFraVedtak.omgjøringsvedtak &&
                !vedtakstype.erIndeksEllerAldersjustering

        fun tilDelvedtakstypeVisningsnavn(): String {
            if (resultatFraVedtak == null) return ""
            return when {
                erKlagevedtak -> "Klagevedtak"
                erOmgjøringsvedtak -> "Omgjøringsvedtak"
                resultatFraVedtak.beregnet && vedtakstype == Vedtakstype.ALDERSJUSTERING -> "Aldersjustering"
                resultatFraVedtak.beregnet && vedtakstype == Vedtakstype.INDEKSREGULERING -> "Indeksregulering"
                klageOmgjøringDetaljer != null &&
                    klageOmgjøringDetaljer.beregnTilDato != null && periode.fom >= klageOmgjøringDetaljer.beregnTilDato
                -> {
                    val prefiks =
                        if (vedtakstype == Vedtakstype.ALDERSJUSTERING) {
                            "Aldersjustering"
                        } else if (vedtakstype == Vedtakstype.INDEKSREGULERING) {
                            "Indeksregulering"
                        } else {
                            "Vedtak"
                        }
                    "$prefiks (${resultatFraVedtak.vedtakstidspunkt?.toLocalDate().tilVisningsnavn()})"
                }
                resultatFraVedtak.vedtaksid == null && resultatKode == Resultatkode.OPPHØR ->
                    // Betyr at opphør er konsekvens av vedtak (opphør perioder før pga virkningstidspunkt settes framover i tid)
                    if (erKlagevedtak) {
                        "Klagevedtak"
                    } else if (erOmgjøringsvedtak) {
                        "Omgjøringsvedtak"
                    } else {
                        "Opphør"
                    }
                vedtakstype == Vedtakstype.ALDERSJUSTERING -> "Aldersjustering"
                vedtakstype == Vedtakstype.INDEKSREGULERING -> "Indeksregulering"
                resultatFraVedtak.vedtakstidspunkt != null
                -> "Vedtak (${resultatFraVedtak.vedtakstidspunkt.toLocalDate().tilVisningsnavn()})"
                else -> "Vedtak"
            }
        }

        @Suppress("unused")
        fun tilResultatkodeVisningsnavn(): String =
            when {
                erOpphør ->
                    if (beregningsdetaljer?.sluttberegning?.ikkeOmsorgForBarnet == true ||
                        beregningsdetaljer?.sluttberegning?.barnetErSelvforsørget == true
                    ) {
                        beregningsdetaljer.sluttberegning.resultatVisningsnavn!!.intern
                    } else if (resultatFraVedtak?.omgjøringsvedtak == true && resultatKode != null) {
                        resultatKode.visningsnavn.intern
                    } else {
                        "Opphør"
                    }
                vedtakstype == Vedtakstype.INNKREVING -> "Innkreving"

                vedtakstype == Vedtakstype.ALDERSJUSTERING -> "Aldersjustering"

                vedtakstype == Vedtakstype.INDEKSREGULERING -> "Indeksregulering"

                resultatKode?.erDirekteAvslag() == true ||
                    resultatKode == Resultatkode.INGEN_ENDRING_UNDER_GRENSE ||
                    resultatKode == Resultatkode.INNVILGET_VEDTAK -> resultatKode.visningsnavnIntern(vedtakstype)

                else -> beregningsdetaljer?.sluttberegning?.resultatVisningsnavn?.intern ?: ""
            }

        data class KlageOmgjøringDetaljer(
            val resultatFraVedtakVedtakstidspunkt: LocalDateTime? = null,
            val beregnTilDato: YearMonth? = null,
            val manuellAldersjustering: Boolean = false,
            val delAvVedtaket: Boolean = true,
            val kanOpprette35c: Boolean = false,
            val skalOpprette35c: Boolean = false,
        )

        data class BidragPeriodeBeregningsdetaljer(
            val bpHarEvne: Boolean,
            val antallBarnIHusstanden: Double? = null,
            val forskuddssats: BigDecimal = BigDecimal.ZERO,
            val barnetilleggBM: DokumentmalDelberegningBarnetilleggDto? = null,
            val barnetilleggBP: DokumentmalDelberegningBarnetilleggDto? = null,
            val voksenIHusstanden: Boolean? = null,
            val enesteVoksenIHusstandenErEgetBarn: Boolean? = null,
            val bpsAndel: DelberegningBidragspliktigesAndel? = null,
            val inntekter: DokumentmalResultatBeregningInntekterDto? = null,
            val delberegningBidragsevne: DokumentmalDelberegningBidragsevneDto? = null,
            val samværsfradrag: NotatBeregningsdetaljerSamværsfradrag? = null,
            val endringUnderGrense: DelberegningEndringSjekkGrensePeriode? = null,
            val sluttberegning: SluttberegningBarnebidrag? = null,
            val delberegningUnderholdskostnad: DelberegningUnderholdskostnad? = null,
            val indeksreguleringDetaljer: IndeksreguleringDetaljer? = null,
            val sluttberegningAldersjustering: SluttberegningBarnebidragAldersjustering? = null,
            val delberegningBidragspliktigesBeregnedeTotalBidrag: DokumentmalDelberegningBidragspliktigesBeregnedeTotalbidragDto? = null,
        ) {
            data class IndeksreguleringDetaljer(
                val sluttberegning: SluttberegningIndeksregulering?,
                val faktor: BigDecimal,
            )

            data class NotatBeregningsdetaljerSamværsfradrag(
                val samværsfradrag: BigDecimal,
                val samværsklasse: Samværsklasse,
                val gjennomsnittligSamværPerMåned: BigDecimal,
            ) {
                val samværsklasseVisningsnavn: String = samværsklasse.visningsnavn.intern
            }

            val deltBosted get() =
                sluttberegning?.bidragJustertForDeltBosted == true ||
                    sluttberegning?.resultat == SluttberegningBarnebidrag::bidragJustertForDeltBosted.name
        }
    }
}

data class DokumentmalDelberegningBarnetilleggDto(
    val barnetillegg: List<DokumentmalBarnetilleggDetaljerDto> = emptyList(),
    val skattFaktor: BigDecimal = BigDecimal.ZERO,
    val delberegningSkattesats: DelberegningBarnetilleggSkattesats? = null,
    val sumBruttoBeløp: BigDecimal = BigDecimal.ZERO,
    val sumNettoBeløp: BigDecimal = BigDecimal.ZERO,
) {
    data class DokumentmalBarnetilleggDetaljerDto(
        val bruttoBeløp: BigDecimal,
        val nettoBeløp: BigDecimal,
        val visningsnavn: String,
    )
}

data class DokumentmalResultatBeregningInntekterDto(
    val inntektBM: BigDecimal? = null,
    val inntektBP: BigDecimal? = null,
    val inntektBarn: BigDecimal? = null,
    val barnEndeligInntekt: BigDecimal? = null,
) {
    val totalEndeligInntekt get() =
        (inntektBM ?: BigDecimal.ZERO) +
            (inntektBP ?: BigDecimal.ZERO) +
            (barnEndeligInntekt ?: BigDecimal.ZERO)
    val inntektBPMånedlig get() = inntektBP?.divide(BigDecimal(12), MathContext(10, RoundingMode.HALF_UP))
    val inntektBMMånedlig get() = inntektBM?.divide(BigDecimal(12), MathContext(10, RoundingMode.HALF_UP))
    val inntektBarnMånedlig get() = inntektBarn?.divide(BigDecimal(12), MathContext(10, RoundingMode.HALF_UP))
}

data class DokumentmalDelberegningBidragsevneDto(
    val sumInntekt25Prosent: BigDecimal,
    val bidragsevne: BigDecimal,
    val skatt: DokumentmalSkattBeregning,
    val underholdEgneBarnIHusstand: DokumentmalUnderholdEgneBarnIHusstand,
    val utgifter: DokumentmalBidragsevneUtgifterBolig,
) {
    data class DokumentmalUnderholdEgneBarnIHusstand(
        val årsbeløp: BigDecimal,
        val sjablon: BigDecimal,
        val antallBarnIHusstanden: Double,
    ) {
        val måndesbeløp get() = årsbeløp.årsbeløpTilMåndesbeløp()
    }

    data class DokumentmalSkattBeregning(
        val sumSkatt: BigDecimal,
        val skattAlminneligInntekt: BigDecimal,
        val trinnskatt: BigDecimal,
        val trygdeavgift: BigDecimal,
    ) {
        val skattMånedsbeløp get() = sumSkatt.årsbeløpTilMåndesbeløp()
        val trinnskattMånedsbeløp get() = trinnskatt.årsbeløpTilMåndesbeløp()
        val skattAlminneligInntektMånedsbeløp get() = skattAlminneligInntekt.årsbeløpTilMåndesbeløp()
        val trygdeavgiftMånedsbeløp get() = trygdeavgift.årsbeløpTilMåndesbeløp()
    }

    data class DokumentmalBidragsevneUtgifterBolig(
        val borMedAndreVoksne: Boolean,
        val boutgiftBeløp: BigDecimal,
        val underholdBeløp: BigDecimal,
    )
}

data class DokumentmalDelberegningBidragspliktigesBeregnedeTotalbidragDto(
    val beregnetBidragPerBarnListe: List<NotatBeregnetBidragPerBarnDto>,
    val bidragspliktigesBeregnedeTotalbidrag: BigDecimal,
    val periode: ÅrMånedsperiode,
) {
    data class NotatBeregnetBidragPerBarnDto(
        val beregnetBidragPerBarn: BeregnetBidragPerBarn,
        val personidentBarn: String,
    )
}

data class DokumentmalPersonDto(
    val rolle: Rolletype? = null,
    val navn: String?,
    val fødselsdato: LocalDate?,
    val ident: Personident?,
    val erBeskyttet: Boolean = false,
    val innbetaltBeløp: BigDecimal? = null,
    val opphørsdato: LocalDate? = null,
    val virkningstidspunkt: LocalDate? = null,
)
