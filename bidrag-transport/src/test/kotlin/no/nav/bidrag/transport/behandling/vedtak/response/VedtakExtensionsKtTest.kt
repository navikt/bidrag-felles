package no.nav.bidrag.transport.behandling.vedtak.response

import com.fasterxml.jackson.databind.node.POJONode
import io.kotest.matchers.shouldBe
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.enums.vedtak.Beslutningstype
import no.nav.bidrag.domene.enums.vedtak.Engangsbeløptype
import no.nav.bidrag.domene.enums.vedtak.Innkrevingstype
import no.nav.bidrag.domene.enums.vedtak.Vedtakskilde
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.organisasjon.Enhetsnummer
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import no.nav.bidrag.transport.behandling.felles.grunnlag.VirkningstidspunktGrunnlag
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth

class VedtakExtensionsKtTest {
    @Test
    fun `skal returnere særbidragperiode fra virkningstidspunkt`() {
        val virkningstidspunkt = YearMonth.parse("2021-04")

        val vedtakDto =
            VedtakDto(
                stønadsendringListe = emptyList(),
                engangsbeløpListe = emptyList(),
                behandlingsreferanseListe = emptyList(),
                grunnlagListe =
                    listOf(
                        GrunnlagDto(
                            referanse = "1",
                            type = Grunnlagstype.VIRKNINGSTIDSPUNKT,
                            innhold =
                                POJONode(
                                    VirkningstidspunktGrunnlag(
                                        virkningstidspunkt = virkningstidspunkt.atDay(1),
                                    ),
                                ),
                        ),
                    ),
                enhetsnummer = Enhetsnummer("1234"),
                vedtakstidspunkt = LocalDateTime.parse("2020-01-01T00:00:00"),
                fastsattILand = "NO",
                innkrevingUtsattTilDato = null,
                kilde = Vedtakskilde.MANUELT,
                kildeapplikasjon = "APP",
                opprettetAv = "USER",
                opprettetAvNavn = "User Name",
                opprettetTidspunkt = LocalDateTime.now(),
                type = Vedtakstype.FASTSETTELSE,
            )

        vedtakDto.særbidragsperiode shouldBe Datoperiode(virkningstidspunkt.atDay(1), virkningstidspunkt.atEndOfMonth())
    }

    @Test
    fun `skal returnere særbidragperiode fra engangsbeløp`() {
        val virkningstidspunkt = YearMonth.parse("2021-04")

        val vedtakDto =
            VedtakDto(
                stønadsendringListe = emptyList(),
                engangsbeløpListe =
                    listOf(
                        EngangsbeløpDto(
                            type = Engangsbeløptype.SÆRBIDRAG,
                            sak = Saksnummer("1234"),
                            beløp = BigDecimal(100),
                            resultatkode = Resultatkode.AVSLAG.name,
                            beslutning = Beslutningstype.ENDRING,
                            delytelseId = "DelytelseId",
                            eksternReferanse = "EksternReferanse",
                            grunnlagReferanseListe = emptyList(),
                            innkreving = Innkrevingstype.MED_INNKREVING,
                            kravhaver = Personident(""),
                            mottaker = Personident(""),
                            omgjørVedtakId = null,
                            referanse = "Referanse",
                            skyldner = Personident(""),
                            valutakode = "Valutakode",
                        ),
                    ),
                behandlingsreferanseListe = emptyList(),
                grunnlagListe = emptyList(),
                enhetsnummer = Enhetsnummer("1234"),
                vedtakstidspunkt = virkningstidspunkt.atDay(1).atStartOfDay(),
                fastsattILand = "NO",
                innkrevingUtsattTilDato = null,
                kilde = Vedtakskilde.MANUELT,
                kildeapplikasjon = "APP",
                opprettetAv = "USER",
                opprettetAvNavn = "User Name",
                opprettetTidspunkt = LocalDateTime.now(),
                type = Vedtakstype.FASTSETTELSE,
            )

        vedtakDto.særbidragsperiode shouldBe Datoperiode(virkningstidspunkt.atDay(1), virkningstidspunkt.atEndOfMonth())
    }

    @Test
    fun `skal returnere null særbidragperiode fra engangsbeløp hvis klage`() {
        val virkningstidspunkt = YearMonth.parse("2021-04")

        val vedtakDto =
            VedtakDto(
                stønadsendringListe = emptyList(),
                engangsbeløpListe =
                    listOf(
                        EngangsbeløpDto(
                            type = Engangsbeløptype.SÆRBIDRAG,
                            sak = Saksnummer("1234"),
                            beløp = BigDecimal(100),
                            resultatkode = Resultatkode.AVSLAG.name,
                            beslutning = Beslutningstype.ENDRING,
                            delytelseId = "DelytelseId",
                            eksternReferanse = "EksternReferanse",
                            grunnlagReferanseListe = emptyList(),
                            innkreving = Innkrevingstype.MED_INNKREVING,
                            kravhaver = Personident(""),
                            mottaker = Personident(""),
                            omgjørVedtakId = 1,
                            referanse = "Referanse",
                            skyldner = Personident(""),
                            valutakode = "Valutakode",
                        ),
                    ),
                behandlingsreferanseListe = emptyList(),
                grunnlagListe = emptyList(),
                enhetsnummer = Enhetsnummer("1234"),
                vedtakstidspunkt = virkningstidspunkt.atDay(1).atStartOfDay(),
                fastsattILand = "NO",
                innkrevingUtsattTilDato = null,
                kilde = Vedtakskilde.MANUELT,
                kildeapplikasjon = "APP",
                opprettetAv = "USER",
                opprettetAvNavn = "User Name",
                opprettetTidspunkt = LocalDateTime.now(),
                type = Vedtakstype.FASTSETTELSE,
            )

        vedtakDto.særbidragsperiode shouldBe null
    }
}
