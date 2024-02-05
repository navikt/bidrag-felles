package no.nav.bidrag.transport.behandling.vedtak

import no.nav.bidrag.domene.enums.vedtak.BehandlingsrefKilde

val VedtakHendelse.søknadId
    get() =
        this.behandlingsreferanseListe?.find {
            it.kilde == BehandlingsrefKilde.BISYS_SØKNAD.name
        }?.referanse?.toLong()

val VedtakHendelse.søknadKlageRefId
    get() =
        this.behandlingsreferanseListe?.find {
            it.kilde == BehandlingsrefKilde.BISYS_KLAGE_REF_SØKNAD.name
        }?.referanse?.toLong()
val VedtakHendelse.behandlingId
    get() =
        this.behandlingsreferanseListe?.find {
            it.kilde == BehandlingsrefKilde.BEHANDLING_ID.name
        }?.referanse?.toLong()

fun VedtakHendelse.erFattetGjennomBidragBehandling() = behandlingId != null

val VedtakHendelse.saksnummer
    get(): String? =
        stønadsendringListe?.firstOrNull()?.sak?.verdi
            ?: engangsbeløpListe?.firstOrNull()?.sak?.verdi
