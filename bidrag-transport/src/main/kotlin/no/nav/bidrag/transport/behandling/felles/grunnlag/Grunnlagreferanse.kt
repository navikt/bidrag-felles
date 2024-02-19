package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.grunnlag.response.AinntektGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.BarnetilleggGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.BarnetilsynGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.KontantstøtteGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.SkattegrunnlagGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.SmåbarnstilleggGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.UtvidetBarnetrygdGrunnlagDto
import no.nav.bidrag.transport.felles.toCompactString

fun Grunnlagstype.tilPersonreferanse(
    identifikator: String,
    id: Int,
) = "person_${this}_${identifikator}_$id"

// Bidrag-grunnlag objekter til referanse
fun SmåbarnstilleggGrunnlagDto.tilGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) =
    "innhentet_småbarnstillegg_${referanseGjelder}_${periodeFra.toCompactString()}"

fun UtvidetBarnetrygdGrunnlagDto.tilGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) =
    "innhentet_utvidetbarnetrygd_${referanseGjelder}_${periodeFra.toCompactString()}"

fun BarnetilsynGrunnlagDto.tilGrunnlagsreferanse(
    referanseGjelder: Grunnlagsreferanse,
    referanseBarn: Grunnlagsreferanse,
) = "innhentet_barnetilsyn_${referanseGjelder}_barn_${referanseBarn}_${periodeFra.toCompactString()}"

fun KontantstøtteGrunnlagDto.tilGrunnlagsreferanse(
    referanseGjelder: Grunnlagsreferanse,
    referanseBarn: Grunnlagsreferanse,
) = "innhentet_kontantstøtte_${referanseGjelder}_barn_${referanseBarn}_${periodeFra.toCompactString()}"

fun BarnetilleggGrunnlagDto.tilGrunnlagsreferanse(
    referanseGjelder: Grunnlagsreferanse,
    referanseBarn: Grunnlagsreferanse,
) = "innhentet_barnetillegg_${referanseGjelder}_" +
    "barn_${referanseBarn}_${periodeFra.toCompactString()}"

fun SkattegrunnlagGrunnlagDto.tilGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) =
    "innhentet_skattegrunnlag_${referanseGjelder}_${periodeFra.toCompactString()}"

fun AinntektGrunnlagDto.tilGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) =
    "innhentet_ainntekt_${referanseGjelder}_${periodeFra.toCompactString()}"

fun opprettInnhentetHusstandsmedlemGrunnlagsreferanse(
    referanseGjelder: Grunnlagsreferanse,
    referanseRelatertTil: Grunnlagsreferanse,
) = "innhentet_husstandsmedlem_${referanseGjelder}_$referanseRelatertTil"

fun opprettArbeidsforholdGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) = "innhentet_arbeidsforhold_$referanseGjelder"

fun opprettInnhentetSivilstandGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) = "innhentet_sivilstand_$referanseGjelder"

// Beregning
fun opprettSluttberegningreferanse(
    barnreferanse: Grunnlagsreferanse,
    periode: ÅrMånedsperiode,
) = "sluttberegning_${barnreferanse}_${periode.fom.toCompactString()}"

fun opprettDelberegningreferanse(
    type: Grunnlagstype,
    periode: ÅrMånedsperiode,
) = "delberegning_${type}_${periode.fom.toCompactString()}"
