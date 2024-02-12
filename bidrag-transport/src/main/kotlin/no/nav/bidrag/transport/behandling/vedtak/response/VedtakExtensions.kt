package no.nav.bidrag.transport.behandling.vedtak.response

import no.nav.bidrag.domene.enums.vedtak.BehandlingsrefKilde

val VedtakDto.saksnummer get() = stønadsendringListe.firstOrNull()?.sak?.verdi ?: engangsbeløpListe.firstOrNull()?.sak?.verdi
val VedtakDto.behandlingId get() =
    behandlingsreferanseListe.find {
        it.kilde == BehandlingsrefKilde.BISYS_SØKNAD
    }?.referanse?.toLong()

val VedtakDto.søknadId get() =
    behandlingsreferanseListe.find {
        it.kilde == BehandlingsrefKilde.BISYS_SØKNAD
    }?.referanse?.toLong()
val VedtakDto.søknadKlageRefId get() =
    behandlingsreferanseListe.find {
        it.kilde == BehandlingsrefKilde.BISYS_KLAGE_REF_SØKNAD
    }?.referanse?.toLong()
