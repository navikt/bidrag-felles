package no.nav.bidrag.transport.behandling.grunnlag

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import no.nav.bidrag.domene.enums.person.Familierelasjon
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBidragspliktigesAndelSærbidrag
import no.nav.bidrag.transport.behandling.felles.grunnlag.InnhentetHusstandsmedlem
import no.nav.bidrag.transport.behandling.grunnlag.response.BorISammeHusstandDto
import no.nav.bidrag.transport.behandling.grunnlag.response.RelatertPersonGrunnlagDto
import no.nav.bidrag.transport.felles.commonObjectmapper
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class JsonMappingTest {
    @Test
    fun `skal returnere andelProsent`() {
        val delberegningObjekt =
            DelberegningBidragspliktigesAndelSærbidrag(
                andelFaktor = 0.833333333333.toBigDecimal(),
                andelBeløp = BigDecimal(3993),
                barnetErSelvforsørget = false,
                periode = ÅrMånedsperiode(LocalDate.parse("2024-08-01"), LocalDate.parse("2024-08-31")),
            )
        delberegningObjekt.andelProsent shouldBe 83.33.toBigDecimal()

        delberegningObjekt
            .copy(
                andelFaktor = 0.4432.toBigDecimal(),
            ).andelProsent shouldBe 44.32.toBigDecimal()

        delberegningObjekt
            .copy(
                andelFaktor = 83.333333333.toBigDecimal(),
            ).andelProsent shouldBe 83.33.toBigDecimal()
    }

    @Test
    fun `skal deserialisere DelberegningBidragspliktigesAndelSærbidrag`() {
        @Language("JSON")
        val json =
            """
            {
                 "periode": {
              "fom": "2024-07",
              "til": "2024-08"
            },
            "andelBeløp": 5796,
            "andelProsent": 64.44,
            "barnetErSelvforsørget": false
                }
            """.trimIndent()

        val delberegning: DelberegningBidragspliktigesAndelSærbidrag = commonObjectmapper.readValue(json)

        delberegning.andelFaktor shouldBe 64.44.toBigDecimal()
        delberegning.andelProsent shouldBe 64.44.toBigDecimal()

        val delberegningObjekt =
            DelberegningBidragspliktigesAndelSærbidrag(
                andelFaktor = 0.833333333333.toBigDecimal(),
                andelBeløp = BigDecimal(3993),
                barnetErSelvforsørget = false,
                periode = ÅrMånedsperiode(LocalDate.parse("2024-08-01"), LocalDate.parse("2024-08-31")),
            )
        delberegningObjekt.andelProsent shouldBe 83.33.toBigDecimal()
        val delberegningJson: String = commonObjectmapper.writerWithDefaultPrettyPrinter().writeValueAsString(delberegningObjekt)

        @Language("JSON")
        val jsonResult =
            """
            {
  "periode" : {
    "fom" : "2024-08",
    "til" : "2024-08"
  },
  "andelFaktor" : 0.833333333333,
  "andelBeløp" : 3993,
  "barnetErSelvforsørget" : false
}
            """.trimIndent().trimStart()
        delberegningJson shouldBe jsonResult
    }

    @Test
    fun `skal deserialisere RelatertPersonGrunnlagDto med deprekerte verdier`() {
        @Language("JSON")
        val json =
            """
            [
            
            {
                "navn": "Testperson1",
                "erBarnAvBmBp": true,
                "fødselsdato": "2018-01-04",
                "partPersonId": "1234",
                "relatertPersonPersonId": "55555",
                "borISammeHusstandDtoListe": [
                  {
                    "periodeFra": "2018-01-04"
                  }
                ]
            },
              {
                "navn": "Testperson2",
                "erBarnAvBmBp": false,
                "fødselsdato": "2018-01-04",
                "partPersonId": "1234",
                "relatertPersonPersonId": "66666",
                "borISammeHusstandDtoListe": [
                  {
                    "periodeFra": "2018-01-04"
                  }
                ]
            }
            ]
            """.trimIndent()
        val relatertPersonObjekt: List<RelatertPersonGrunnlagDto> = commonObjectmapper.readValue(json)

        assertSoftly(relatertPersonObjekt[0]) {
            erBarnAvBmBp shouldBe true
            erBarn shouldBe true
            relasjon shouldBe Familierelasjon.BARN
            relatertPersonPersonId shouldBe "55555"
            gjelderPersonId shouldBe "55555"
        }
        assertSoftly(relatertPersonObjekt[1]) {
            erBarnAvBmBp shouldBe false
            erBarn shouldBe false
            relasjon shouldBe Familierelasjon.UKJENT
            relatertPersonPersonId shouldBe "66666"
            gjelderPersonId shouldBe "66666"
        }
    }

    @Test
    fun `skal serialisere RelatertPersonGrunnlagDto med deprekerte verdier`() {
        val relatertPersonGrunnlagListe =
            listOf(
                RelatertPersonGrunnlagDto(
                    navn = "Testperson1",
                    erBarnAvBmBp = true,
                    fødselsdato = LocalDate.parse("2023-01-01"),
                    partPersonId = "1234",
                    relatertPersonPersonId = "55555",
                    borISammeHusstandDtoListe =
                        listOf(
                            BorISammeHusstandDto(
                                periodeFra = LocalDate.parse("2023-01-01"),
                                periodeTil = LocalDate.parse("2024-01-01"),
                            ),
                        ),
                ),
                RelatertPersonGrunnlagDto(
                    navn = "Testperson2",
                    erBarnAvBmBp = false,
                    fødselsdato = LocalDate.parse("2023-01-01"),
                    partPersonId = "1234",
                    relatertPersonPersonId = "666666",
                    borISammeHusstandDtoListe =
                        listOf(
                            BorISammeHusstandDto(
                                periodeFra = LocalDate.parse("2023-01-01"),
                                periodeTil = LocalDate.parse("2024-01-01"),
                            ),
                        ),
                ),
            )
        val jsonString = commonObjectmapper.writerWithDefaultPrettyPrinter().writeValueAsString(relatertPersonGrunnlagListe)

        @Language("JSON")
        val expectedJson =
            """
            [ {
              "partPersonId" : "1234",
              "gjelderPersonId" : "55555",
              "navn" : "Testperson1",
              "fødselsdato" : "2023-01-01",
              "relasjon" : "BARN",
              "borISammeHusstandDtoListe" : [ {
                "periodeFra" : "2023-01-01",
                "periodeTil" : "2024-01-01"
              } ]
            }, {
              "partPersonId" : "1234",
              "gjelderPersonId" : "666666",
              "navn" : "Testperson2",
              "fødselsdato" : "2023-01-01",
              "relasjon" : "UKJENT",
              "borISammeHusstandDtoListe" : [ {
                "periodeFra" : "2023-01-01",
                "periodeTil" : "2024-01-01"
              } ]
            } ]
            """.trimIndent()
        jsonString shouldBe expectedJson

        val relatertPersonObjekt: List<RelatertPersonGrunnlagDto> = commonObjectmapper.readValue(jsonString)

        assertSoftly(relatertPersonObjekt[0]) {
            erBarnAvBmBp shouldBe true
            erBarn shouldBe true
            relasjon shouldBe Familierelasjon.BARN
            relatertPersonPersonId shouldBe "55555"
            gjelderPersonId shouldBe "55555"
        }
        assertSoftly(relatertPersonObjekt[1]) {
            erBarnAvBmBp shouldBe false
            erBarn shouldBe false
            relasjon shouldBe Familierelasjon.UKJENT
            relatertPersonPersonId shouldBe "666666"
            gjelderPersonId shouldBe "666666"
        }
    }

    @Test
    fun `skal deserialisere HusstandsmedlemPDL med deprekerte verdier`() {
        @Language("JSON")
        val json =
            """
            [
            
            {
                "navn": "Testperson1",
                "erBarnAvBmBp": true,
                "fødselsdato": "2018-01-04",
                "relatertPerson": "REF_ID",
                "perioder": [
                  {
                    "fom": "2018-01-04",
                    "til": null
                  }
                ]
            },
              {
                "navn": "Testperson2",
                "erBarnAvBmBp": false,
                "fødselsdato": "2018-01-04",
                "relatertPerson": "REF_ID2",
                "perioder": [
                  {
                    "fom": "2018-01-04",
                     "til": null
                  }
                ]
            }
            ]
            """.trimIndent()
        val relatertPersonObjekt: List<InnhentetHusstandsmedlem.HusstandsmedlemPDL> = commonObjectmapper.readValue(json)

        assertSoftly(relatertPersonObjekt[0]) {
            erBarnAvBmBp shouldBe true
            erBarn shouldBe true
            relasjon shouldBe Familierelasjon.BARN
            gjelderPerson shouldBe "REF_ID"
            relatertPerson shouldBe "REF_ID"
        }
        assertSoftly(relatertPersonObjekt[1]) {
            erBarnAvBmBp shouldBe false
            erBarn shouldBe false
            relasjon shouldBe Familierelasjon.UKJENT
            gjelderPerson shouldBe "REF_ID2"
            relatertPerson shouldBe "REF_ID2"
        }
    }

    @Test
    fun `skal serialisere HusstandsmedlemPDL med deprekerte verdier`() {
        val relatertPersonGrunnlagListe =
            listOf(
                InnhentetHusstandsmedlem.HusstandsmedlemPDL(
                    navn = "Testperson1",
                    erBarnAvBmBp = true,
                    fødselsdato = LocalDate.parse("2023-01-01"),
                    relatertPerson = "REF1",
                    perioder =
                        listOf(
                            Datoperiode(
                                LocalDate.parse("2023-01-01"),
                                null,
                            ),
                        ),
                ),
                InnhentetHusstandsmedlem.HusstandsmedlemPDL(
                    navn = "Testperson2",
                    erBarnAvBmBp = false,
                    fødselsdato = LocalDate.parse("2023-01-01"),
                    relatertPerson = "REF2",
                    perioder =
                        listOf(
                            Datoperiode(
                                LocalDate.parse("2023-01-01"),
                                null,
                            ),
                        ),
                ),
            )
        val jsonString = commonObjectmapper.writerWithDefaultPrettyPrinter().writeValueAsString(relatertPersonGrunnlagListe)

        @Language("JSON")
        val expectedJson =
            """
            [ {
              "gjelderPerson" : "REF1",
              "relasjon" : "BARN",
              "navn" : "Testperson1",
              "fødselsdato" : "2023-01-01",
              "perioder" : [ {
                "fom" : "2023-01-01",
                "til" : null
              } ]
            }, {
              "gjelderPerson" : "REF2",
              "relasjon" : "UKJENT",
              "navn" : "Testperson2",
              "fødselsdato" : "2023-01-01",
              "perioder" : [ {
                "fom" : "2023-01-01",
                "til" : null
              } ]
            } ]
            """.trimIndent()
        jsonString shouldBe expectedJson

        val husstandsmedlemPDLList: List<InnhentetHusstandsmedlem.HusstandsmedlemPDL> = commonObjectmapper.readValue(jsonString)

        assertSoftly(husstandsmedlemPDLList[0]) {
            erBarnAvBmBp shouldBe true
            erBarn shouldBe true
            relasjon shouldBe Familierelasjon.BARN
            gjelderPerson shouldBe "REF1"
            relatertPerson shouldBe "REF1"
        }
        assertSoftly(husstandsmedlemPDLList[1]) {
            erBarnAvBmBp shouldBe false
            erBarn shouldBe false
            relasjon shouldBe Familierelasjon.UKJENT
            gjelderPerson shouldBe "REF2"
            relatertPerson shouldBe "REF2"
        }
    }
}
