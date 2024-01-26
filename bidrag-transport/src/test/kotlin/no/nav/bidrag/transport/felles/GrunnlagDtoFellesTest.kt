package no.nav.bidrag.transport.felles

import com.fasterxml.jackson.databind.node.POJONode
import io.kotest.matchers.shouldBe
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import no.nav.bidrag.transport.behandling.felles.grunnlag.Person
import org.junit.jupiter.api.Test
import java.time.LocalDate

class GrunnlagDtoFellesTest {
    @Test
    fun `Skal opprette grunnlag`() {
        val grunnlag =
            GrunnlagDto(
                "Person-150-BM",
                Grunnlagstype.PERSON,
                POJONode(Person(Personident("123213213213"), "Navn Navnesen", LocalDate.parse("2020-01-01"))),
            )

        grunnlag.toString() shouldBe "PERSON - referanse=Person-150-BM, grunnlagsreferanseListe=<tomt>, " +
            "innhold={\"ident\":\"123213213213\",\"navn\":\"Navn Navnesen\",\"fødselsdato\":\"2020-01-01\"}"
    }
}
