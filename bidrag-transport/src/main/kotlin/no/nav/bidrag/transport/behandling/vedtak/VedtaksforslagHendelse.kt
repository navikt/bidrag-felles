package no.nav.bidrag.transport.behandling.vedtak

import no.nav.bidrag.domene.enums.vedtak.Vedtaksforslagsstatus
import no.nav.bidrag.domene.sak.Saksnummer

data class VedtaksforslagHendelse(
    val status: Vedtaksforslagsstatus,
    val id: Int,
    val saksnummerListe: List<Saksnummer>,
    val sporingsdata: Sporingsdata,
)
