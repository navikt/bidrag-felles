package no.nav.bidrag.transport.sak

import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.ident.ReellMottaker
import no.nav.bidrag.domene.ident.SamhandlerId
import no.nav.bidrag.domene.sak.Saksnummer

data class Sakshendelse(
    val saksnummer: Saksnummer,
    val hendelsestype: SakKafkaHendelsestype,
    val roller: List<Saksrolle> = emptyList(),
    val sporingId: String,
)

data class Saksrolle(
    val ident: Personident? = null,
    val type: Rolletype,
    val samhandlerId: SamhandlerId? = null,
    val reelMottager: ReellMottaker? = null,
    val ukjent: Boolean = false,
)

enum class SakKafkaHendelsestype {
    ENDRING,
    OPPRETTELSE,
}
