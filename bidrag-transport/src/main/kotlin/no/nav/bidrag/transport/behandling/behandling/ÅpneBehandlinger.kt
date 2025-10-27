package no.nav.bidrag.transport.behandling.behandling

import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import java.time.LocalDate

data class HentÅpneBehandlingerRequest(
    val barnIdent: String,
)

data class HentÅpneBehandlingerRespons(
    val behandlinger: List<ÅpenBehandling>,
)

data class ÅpenBehandling(
    val stønadstype: Stønadstype,
    val behandlingId: Long,
    val barn: List<ÅpenBehandlingBarn>,
)

data class ÅpenBehandlingBarn(
    val saksnummer: String,
    val søknadsid: Long,
    val bidragsmottakerIdent: String,
    val ident: String,
    val søktFraDato: LocalDate,
    val mottattDato: LocalDate,
)
