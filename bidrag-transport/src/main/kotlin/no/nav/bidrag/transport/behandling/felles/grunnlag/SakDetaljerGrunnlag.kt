package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.tid.ÅrMånedsperiode

data class SakDetaljerGrunnlag(
    override val periode: ÅrMånedsperiode,
    override val manueltRegistrert: Boolean = false,
    val saksnummer: String,
    val gjelderUtland: Boolean = false,
) : GrunnlagPeriodeInnhold
