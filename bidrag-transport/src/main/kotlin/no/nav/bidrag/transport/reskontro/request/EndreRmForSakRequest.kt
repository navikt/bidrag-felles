package no.nav.bidrag.reskontro.dto.request

import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.sak.Saksnummer

data class EndreRmForSakRequest(
    val saksnummer: Saksnummer,
    val barn: Personident,
    val nyttFødselsnummer: Personident,
)
