package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.felles.toCompactString

fun opprettSluttberegningreferanse(
    barnreferanse: String,
    periode: ÅrMånedsperiode,
) = "sluttberegning_${barnreferanse}_${periode.fom.toCompactString()}"

fun opprettDelberegningreferanse(
    type: Grunnlagstype,
    periode: ÅrMånedsperiode,
) = "delberegning_${type}_${periode.fom.toCompactString()}"
