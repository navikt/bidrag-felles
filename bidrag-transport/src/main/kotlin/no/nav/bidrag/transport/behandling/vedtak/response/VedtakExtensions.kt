package no.nav.bidrag.transport.behandling.vedtak.response

import no.nav.bidrag.domene.enums.behandling.TypeBehandling
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.beregning.Resultatkode.Companion.erDirekteAvslag
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.enums.vedtak.BehandlingsrefKilde
import no.nav.bidrag.domene.enums.vedtak.Engangsbeløptype
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.VirkningstidspunktGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.filtrerBasertPåEgenReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.innholdTilObjektLegacy
import java.time.YearMonth

val VedtakDto.saksnummer get() = stønadsendringListe.firstOrNull()?.sak?.verdi ?: engangsbeløpListe.firstOrNull()?.sak?.verdi
val VedtakDto.behandlingId get() =
    behandlingsreferanseListe
        .find {
            it.kilde == BehandlingsrefKilde.BISYS_SØKNAD
        }?.referanse
        ?.toLong()

val VedtakDto.søknadId get() =
    behandlingsreferanseListe
        .find {
            it.kilde == BehandlingsrefKilde.BISYS_SØKNAD
        }?.referanse
        ?.toLong()
val VedtakDto.søknadKlageRefId get() =
    behandlingsreferanseListe
        .find {
            it.kilde == BehandlingsrefKilde.BISYS_KLAGE_REF_SØKNAD
        }?.referanse
        ?.toLong()

val VedtakDto.virkningstidspunkt get() =
    grunnlagListe
        .filtrerBasertPåEgenReferanse(
            Grunnlagstype.VIRKNINGSTIDSPUNKT,
        ).firstOrNull()
        ?.innholdTilObjektLegacy<VirkningstidspunktGrunnlag>()
        ?.virkningstidspunkt

val VedtakDto.særbidragsperiode get() =
    virkningstidspunkt?.let {
        Datoperiode(
            vedtakstidspunkt.withDayOfMonth(1).toLocalDate(),
            YearMonth.from(vedtakstidspunkt).atEndOfMonth(),
        )
    }
        ?: if (engangsbeløpListe.firstOrNull()?.omgjørVedtakId == null) {
            Datoperiode(YearMonth.from(vedtakstidspunkt), YearMonth.from(vedtakstidspunkt))
        } else {
            null
        }
val VedtakDto.erDirekteAvslag get(): Boolean {
    val virkningstidspunkt =
        grunnlagListe
            .filtrerBasertPåEgenReferanse(
                Grunnlagstype.VIRKNINGSTIDSPUNKT,
            ).firstOrNull()
            ?.innholdTilObjektLegacy<VirkningstidspunktGrunnlag>()
    return virkningstidspunkt?.avslag != null ||
        engangsbeløpListe
            .firstOrNull()
            ?.resultatkode
            ?.let { Resultatkode.fraKode(it)?.erDirekteAvslag() } ?: false
}

val VedtakDto.typeBehandling get() =
    when {
        stønadsendringListe.isNotEmpty() && stønadsendringListe.first().type == Stønadstype.FORSKUDD -> TypeBehandling.FORSKUDD
        engangsbeløpListe.isNotEmpty() && engangsbeløpListe.first().type == Engangsbeløptype.SÆRBIDRAG -> TypeBehandling.SÆRBIDRAG
        else -> TypeBehandling.BIDRAG
    }
