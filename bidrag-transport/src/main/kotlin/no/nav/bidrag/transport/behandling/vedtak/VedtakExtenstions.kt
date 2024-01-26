package no.nav.bidrag.transport.behandling.vedtak

import com.fasterxml.jackson.module.kotlin.treeToValue
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagInnhold
import no.nav.bidrag.transport.behandling.felles.grunnlag.InntektRapporteringPeriodeGrunnlag
import no.nav.bidrag.transport.behandling.vedtak.response.VedtakDto
import no.nav.bidrag.transport.felles.commonObjectmapper

fun VedtakDto.hentInntekter(): List<InntektRapporteringPeriodeGrunnlag> = hentGrunnagDetaljer(Grunnlagstype.INNTEKT_RAPPORTERING_PERIODE)

inline fun <reified T : GrunnlagInnhold> VedtakDto.hentGrunnagDetaljer(
    grunnlagType: Grunnlagstype,
    referanser: List<String> = emptyList(),
): List<T> =
    grunnlagListe
        .filter { it.type == grunnlagType }
        .filter { referanser.isEmpty() || referanser.contains(it.referanse) }
        .map { commonObjectmapper.treeToValue(it.innhold) }
