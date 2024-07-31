package no.nav.bidrag.transport.behandling.vedtak.response

import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.beregning.Resultatkode.Companion.erDirekteAvslag
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.enums.vedtak.BehandlingsrefKilde
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.SærbidragsperiodeGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.VirkningstidspunktGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.filtrerBasertPåEgenReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.innholdTilObjekt
import no.nav.bidrag.transport.felles.ifTrue
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
        ?.innholdTilObjekt<VirkningstidspunktGrunnlag>()
        ?.virkningstidspunkt

val VedtakDto.særbidragsperiode get() =
    grunnlagListe
        .filtrerBasertPåEgenReferanse(
            Grunnlagstype.SÆRBIDRAGSPERIODE,
        ).firstOrNull()
        ?.innholdTilObjekt<SærbidragsperiodeGrunnlag>()
        ?.periode
        ?: (engangsbeløpListe.firstOrNull()?.omgjørVedtakId == null).ifTrue {
            Datoperiode(YearMonth.from(vedtakstidspunkt), YearMonth.from(vedtakstidspunkt))
        }
val VedtakDto.erDirekteAvslag get() =
    grunnlagListe
        .filtrerBasertPåEgenReferanse(
            Grunnlagstype.VIRKNINGSTIDSPUNKT,
        ).firstOrNull()
        ?.innholdTilObjekt<VirkningstidspunktGrunnlag>()
        ?.let { it.avslag != null }
        ?: engangsbeløpListe.firstOrNull()?.resultatkode?.let { Resultatkode.fraKode(it)?.erDirekteAvslag() }
        ?: false
