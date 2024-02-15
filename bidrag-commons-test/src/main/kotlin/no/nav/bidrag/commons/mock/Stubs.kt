package no.nav.bidrag.commons.web.mock

import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import no.nav.bidrag.commons.service.KodeverkProvider
import no.nav.bidrag.commons.service.finnVisningsnavn
import no.nav.bidrag.commons.service.finnVisningsnavnLønnsbeskrivelse
import no.nav.bidrag.commons.service.finnVisningsnavnSkattegrunnlag
import no.nav.bidrag.commons.service.sjablon.SjablonProvider
import no.nav.bidrag.commons.service.sjablon.Sjablontall
import no.nav.bidrag.transport.felles.commonObjectmapper

class Stubs

fun stubKodeverkProvider() {
    mockkObject(KodeverkProvider)
    mockkStatic(::finnVisningsnavnSkattegrunnlag)
    mockkStatic(::finnVisningsnavnLønnsbeskrivelse)
    mockkStatic(::finnVisningsnavn)
    every { finnVisningsnavn(any()) } returns "Visningsnavn"
    every {
        finnVisningsnavnLønnsbeskrivelse(any())
    } returns "Visningsnavn lønnsbeskrivelse"
    every { finnVisningsnavnSkattegrunnlag(any()) } returns "Visningsnavn skattegrunnlag"
}

fun stubSjablonProvider() {
    mockkObject(SjablonProvider)
    every {
        SjablonProvider.hentSjablontall()
    } returns sjablonResponse()
}

fun sjablonResponse(): List<Sjablontall> {
    val fil = hentFil("/__files/sjablon.json")
    return commonObjectmapper.readValue(fil)
}

fun hentFil(filsti: String) =
    Stubs::class.java.getResource(
        filsti,
    ) ?: throw RuntimeException("Fant ingen fil på sti $filsti")
