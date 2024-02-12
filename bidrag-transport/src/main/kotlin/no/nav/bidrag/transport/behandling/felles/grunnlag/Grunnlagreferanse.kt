package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.grunnlag.response.AinntektGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.ArbeidsforholdGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.BarnetilleggGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.BarnetilsynGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.BorISammeHusstandDto
import no.nav.bidrag.transport.behandling.grunnlag.response.KontantstøtteGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.RelatertPersonGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.SivilstandGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.SkattegrunnlagGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.SmåbarnstilleggGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.UtvidetBarnetrygdGrunnlagDto
import no.nav.bidrag.transport.felles.toCompactString
import java.time.LocalDate

fun Grunnlagstype.tilPersonreferanse(
    fødseldato: String,
    id: Int,
) = "person_${this}_${fødseldato}_$id"

fun RelatertPersonGrunnlagDto.tilGrunnlagsreferanse(index: Int) =
    Grunnlagstype.PERSON_HUSSTANDSMEDLEM.tilPersonreferanse(
        fødselsdato?.toCompactString() ?: LocalDate.MIN.toCompactString(),
        index,
    )

fun SivilstandGrunnlagDto.tilGrunnlagsreferanse(
    referanseGjelder: Grunnlagsreferanse,
    index: Int,
) = "innhentet_sivilstand_${referanseGjelder}_${type}_${gyldigFom.toCompactString()}_$index"

fun BorISammeHusstandDto.tilGrunnlagsreferanse(
    referanseGjelder: Grunnlagsreferanse,
    referanseRelatertTil: Grunnlagsreferanse,
) = "innhentet_husstandsmedlem_${referanseGjelder}_${referanseRelatertTil}_${periodeFra.toCompactString()}"

fun ArbeidsforholdGrunnlagDto.tilGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) =
    "innhentet_arbeidsforhold_${referanseGjelder}_${arbeidsgiverOrgnummer}_${startdato.toCompactString()}"

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

fun opprettSluttberegningreferanse(
    barnreferanse: Grunnlagsreferanse,
    periode: ÅrMånedsperiode,
) = "sluttberegning_${barnreferanse}_${periode.fom.toCompactString()}"

fun opprettDelberegningreferanse(
    type: Grunnlagstype,
    periode: ÅrMånedsperiode,
) = "delberegning_${type}_${periode.fom.toCompactString()}"
