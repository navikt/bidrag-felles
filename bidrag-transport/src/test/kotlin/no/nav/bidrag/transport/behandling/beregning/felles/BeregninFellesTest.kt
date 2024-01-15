package no.nav.bidrag.transport.behandling.beregning.felles

import com.fasterxml.jackson.module.kotlin.convertValue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.transport.behandling.felles.grunnlag.Person
import no.nav.bidrag.transport.felles.objectmapper
import org.junit.jupiter.api.Test
import java.time.LocalDate

class BeregninFellesTest {
    @Test
    fun `skal konvertere grunnlag`() {
        val personreferanse = "person_bm_1"
        val beregnGrunnlag =
            BeregnGrunnlag(
                grunnlagListe =
                    listOf(
                        Grunnlag(
                            referanse = personreferanse,
                            type = Grunnlagstype.PERSON,
                            innhold =
                                objectmapper.convertValue(
                                    Person(Personident("123123123"), fødselsdato = LocalDate.parse("2023-01-01")),
                                ),
                        ),
                    ),
            )

        val person = beregnGrunnlag.hentInnholdBasertPåEgenReferanse(Grunnlagstype.PERSON, Person::class.java, personreferanse)
        person shouldHaveSize 1
        person.firstOrNull()?.innhold?.ident?.verdi shouldBe "123123123"
    }
}
