package no.nav.bidrag.transport.behandling.behandling

import no.nav.bidrag.domene.enums.vedtak.Stønadstype

data class HentÅpneBehandlingerRequest(
    val barnIdent: String,
)

data class HentÅpneBehandlingerRespons(
    val åpneBehandling: List<ÅpenBehandling>,
)

data class ÅpenBehandling(
    val stønadstype: Stønadstype,
    val behandlingId: Long,
)
