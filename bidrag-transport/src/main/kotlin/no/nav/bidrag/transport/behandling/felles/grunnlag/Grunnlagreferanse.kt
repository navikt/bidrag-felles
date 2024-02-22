package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.enums.grunnlag.GrunnlagDatakilde
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.felles.toCompactString

fun Grunnlagstype.tilPersonreferanse(
    identifikator: String,
    id: Int,
) = "person_${this}_${identifikator}_$id"

// Bidrag-grunnlag objekter til referanse
fun opprettSmåbarnstilleggGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) = "innhentet_småbarnstillegg_$referanseGjelder"

fun opprettUtvidetbarnetrygGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) = "innhentet_utvidetbarnetrygd_$referanseGjelder"

fun opprettBarnetilsynGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) = "innhentet_barnetilsyn_$referanseGjelder"

fun opprettKontantstøtteGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) = "innhentet_kontantstøtte_$referanseGjelder"

fun opprettBarnetilleggGrunnlagsreferanse(
    referanseGjelder: Grunnlagsreferanse,
    kilde: GrunnlagDatakilde = GrunnlagDatakilde.PENSJON,
) = "innhentet_barnetillegg_${referanseGjelder}_$kilde"

fun opprettSkattegrunnlagGrunnlagsreferanse(
    referanseGjelder: Grunnlagsreferanse,
    år: Int,
) = "innhentet_skattegrunnlag_${referanseGjelder}_$år"

fun opprettAinntektGrunnlagsreferanse(referanseGjelder: Grunnlagsreferanse) = "innhentet_ainntekt_$referanseGjelder"

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
