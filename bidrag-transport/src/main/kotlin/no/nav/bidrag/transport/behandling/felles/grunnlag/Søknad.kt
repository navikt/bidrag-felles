package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.rolle.SøktAvType
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.enums.vedtak.VirkningstidspunktÅrsakstype
import java.time.LocalDate
import java.time.YearMonth

data class SøknadGrunnlag(
    val mottattDato: LocalDate,
    val søktFraDato: LocalDate,
    val søktAv: SøktAvType,
    @Schema(
        description =
            "Dato når klage ble mottatt. Kan være ulik mottattDato hvis behandlingen gjelder særbidrag." +
                "I særbidrag så er det mottatt dato på originale søknad som gjelder",
    )
    val klageMottattDato: LocalDate? = null,
    @Schema(
        description =
            "Opprinnelig vedtakstype hvis behandlingen gjelder klage." +
                " Dette er relevant for beregning av blant annet for særbidrag" +
                " hvor resultatet kan være lavere enn forskuddssats hvis vedtakstype er ENDRING",
    )
    val opprinneligVedtakstype: Vedtakstype? = null,
    val begrensetRevurdering: Boolean = false,
    val egetTiltak: Boolean = false,
    val paragraf35c: Boolean = false,
    val privatAvtale: Boolean = false,
) : GrunnlagInnhold

data class VirkningstidspunktGrunnlag(
    val virkningstidspunkt: LocalDate,
    val opprinneligVirkningstidspunkt: LocalDate? = null,
    val opphørsdato: LocalDate? = null,
    val beregnTilDato: YearMonth? = null,
    val årsak: VirkningstidspunktÅrsakstype? = null,
    val avslag: Resultatkode? = null,
) : GrunnlagInnhold
