package no.nav.bidrag.transport.organisasjon

import no.nav.bidrag.domene.enums.diverse.Tema
import no.nav.bidrag.domene.enums.sak.Sakskategori
import no.nav.bidrag.domene.enums.vedtak.Behandlingstype
import no.nav.bidrag.domene.ident.Personident

data class HentEnhetRequest(
    val ident: Personident,
    val biidenter: Set<Personident> = setOf(),
    val tema: String = Tema.TEMA_BIDRAG.verdi,
    val behandlingstema: String? = null,
    val sakskategori: Sakskategori? = null,
    val behandlingstype: Behandlingstype? = null,

) {

    val alleIdenter get() = biidenter + ident
}
