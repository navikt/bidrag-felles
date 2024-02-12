package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.databind.node.POJONode
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.transport.behandling.grunnlag.response.AinntektGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.BarnetilleggGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.KontantstøtteGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.SkattegrunnlagGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.SmåbarnstilleggGrunnlagDto
import no.nav.bidrag.transport.behandling.grunnlag.response.UtvidetBarnetrygdGrunnlagDto
import java.time.LocalDateTime

fun AinntektGrunnlagDto.tilGrunnlagsobjekt(
    hentetTidspunkt: LocalDateTime,
    gjelderReferanse: String,
) = GrunnlagDto(
    referanse = tilGrunnlagsreferanse(gjelderReferanse),
    type = Grunnlagstype.INNHENTET_INNTEKT_AINNTEKT_PERIODE,
    gjelderReferanse = gjelderReferanse,
    innhold =
        POJONode(
            InnhentetAinntekt(
                periode = Datoperiode(periodeFra, periodeTil),
                hentetTidspunkt = hentetTidspunkt,
                grunnlag =
                    InnhentetAinntekt.AinntektInnhentet(
                        ainntektspostListe =
                            ainntektspostListe.map { post ->
                                InnhentetAinntekt.Ainntektspost(
                                    utbetalingsperiode = post.utbetalingsperiode,
                                    opptjeningsperiodeFra = post.opptjeningsperiodeFra,
                                    opptjeningsperiodeTil = post.opptjeningsperiodeTil,
                                    kategori = post.kategori,
                                    fordelType = post.fordelType,
                                    beløp = post.beløp,
                                    etterbetalingsperiodeFra = post.etterbetalingsperiodeFra,
                                    etterbetalingsperiodeTil = post.etterbetalingsperiodeTil,
                                )
                            },
                    ),
            ),
        ),
)

fun SmåbarnstilleggGrunnlagDto.tilGrunnlagsobjekt(
    hentetTidspunkt: LocalDateTime,
    gjelderReferanse: Grunnlagsreferanse,
) = GrunnlagDto(
    referanse = tilGrunnlagsreferanse(gjelderReferanse),
    type = Grunnlagstype.INNHENTET_INNTEKT_SMÅBARNSTILLEGG_PERIODE,
    gjelderReferanse = gjelderReferanse,
    innhold =
        POJONode(
            InnhentetSmåbarnstillegg(
                periode = Datoperiode(periodeFra, periodeTil),
                hentetTidspunkt = hentetTidspunkt,
                grunnlag =
                    InnhentetSmåbarnstillegg.Småbarnstillegg(
                        beløp = beløp,
                        manueltBeregnet = manueltBeregnet,
                    ),
            ),
        ),
)

fun UtvidetBarnetrygdGrunnlagDto.tilGrunnlagsobjekt(
    hentetTidspunkt: LocalDateTime,
    gjelderReferanse: String,
) = GrunnlagDto(
    referanse = tilGrunnlagsreferanse(gjelderReferanse),
    type = Grunnlagstype.INNHENTET_INNTEKT_UTVIDETBARNETRYGD_PERIODE,
    gjelderReferanse = gjelderReferanse,
    innhold =
        POJONode(
            InnhentetUtvidetBarnetrygd(
                periode = Datoperiode(periodeFra, periodeTil),
                hentetTidspunkt = hentetTidspunkt,
                grunnlag =
                    InnhentetUtvidetBarnetrygd.UtvidetBarnetrygd(
                        beløp = beløp,
                        manueltBeregnet = manueltBeregnet,
                    ),
            ),
        ),
)

fun BarnetilleggGrunnlagDto.tilGrunnlagsobjekt(
    hentetTidspunkt: LocalDateTime,
    gjelderReferanse: String,
    søknadsbarnReferanse: String,
) = GrunnlagDto(
    referanse = tilGrunnlagsreferanse(gjelderReferanse, søknadsbarnReferanse),
    type = Grunnlagstype.INNHENTET_INNTEKT_BARNETILLEGG_PERIODE,
    grunnlagsreferanseListe = listOf(gjelderReferanse, søknadsbarnReferanse),
    gjelderReferanse = gjelderReferanse,
    innhold =
        POJONode(
            InnhentetBarnetillegg(
                periode = Datoperiode(periodeFra, periodeTil),
                hentetTidspunkt = hentetTidspunkt,
                grunnlag =
                    InnhentetBarnetillegg.Barnetillegg(
                        gjelderBarn = søknadsbarnReferanse,
                        barnetilleggType = barnetilleggType,
                        barnType = barnType,
                        beløpBrutto = beløpBrutto,
                    ),
            ),
        ),
)

fun KontantstøtteGrunnlagDto.tilGrunnlagsobjekt(
    hentetTidspunkt: LocalDateTime,
    gjelderReferanse: String,
    søknadsbarnReferanse: String,
) = GrunnlagDto(
    referanse = tilGrunnlagsreferanse(gjelderReferanse, søknadsbarnReferanse),
    type = Grunnlagstype.INNHENTET_INNTEKT_KONTANTSTØTTE_PERIODE,
    grunnlagsreferanseListe = listOf(gjelderReferanse),
    gjelderReferanse = gjelderReferanse,
    innhold =
        POJONode(
            InnhentetKontantstøtte(
                periode = Datoperiode(periodeFra, periodeTil),
                hentetTidspunkt = hentetTidspunkt,
                grunnlag =
                    InnhentetKontantstøtte.Kontantstøtte(
                        gjelderBarn = søknadsbarnReferanse,
                        beløp = beløp,
                    ),
            ),
        ),
)

fun SkattegrunnlagGrunnlagDto.tilGrunnlagsobjekt(
    hentetTidspunkt: LocalDateTime,
    gjelderReferanse: String,
) = GrunnlagDto(
    referanse = tilGrunnlagsreferanse(gjelderReferanse),
    type = Grunnlagstype.INNHENTET_INNTEKT_SKATTEGRUNNLAG_PERIODE,
    gjelderReferanse = gjelderReferanse,
    innhold =
        POJONode(
            InnhentetSkattegrunnlag(
                periode = Datoperiode(periodeFra, periodeTil),
                hentetTidspunkt = hentetTidspunkt,
                grunnlag =
                    InnhentetSkattegrunnlag.Skattegrunnlag(
                        skattegrunnlagListe =
                            skattegrunnlagspostListe.map { post ->
                                InnhentetSkattegrunnlag.Skattegrunnlagspost(
                                    skattegrunnlagType = post.skattegrunnlagType,
                                    kode = post.kode,
                                    beløp = post.beløp,
                                )
                            },
                    ),
            ),
        ),
)
