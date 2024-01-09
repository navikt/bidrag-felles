@file:Suppress("unused")

package no.nav.bidrag.domene.enums.sak

import no.nav.bidrag.domene.enums.vedtak.Behandlingstype

enum class Sakskategori(
    private val beskrivelse: String,
    private val behandlingstypeForvaltning: String?,
    private val behandlingstypeKlage: String,
    private val behandlingstypeSøknad: String,
    val behandlingstype: String,
) {
    N("Nasjonal", null, "ae0058", "ae0003", "ae0118"),
    U("Utland", "ae0106", "ae0108", "ae0110", "ae0106"),
    ;

    override fun toString(): String {
        return beskrivelse
    }

    fun finnBehandlingstypekode(behandlingstype: Behandlingstype): String? {
        return when (behandlingstype) {
            Behandlingstype.FORVALTNING -> this.behandlingstypeForvaltning
            Behandlingstype.KLAGE -> this.behandlingstypeKlage
            Behandlingstype.SØKNAD -> this.behandlingstypeSøknad
        }
    }
}
