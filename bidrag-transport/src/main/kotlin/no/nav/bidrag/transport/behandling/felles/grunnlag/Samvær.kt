package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.beregning.samvær.SamværskalkulatorDetaljer

data class SamværPeriodeGrunnlag(
    override val periode: ÅrMånedsperiode,
    override val manueltRegistrert: Boolean = true,
    val samværsklasse: Samværsklasse,
    val beregning: SamværskalkulatorDetaljerGrunnlag? = null,
) : GrunnlagPeriodeInnhold

typealias SamværskalkulatorDetaljerGrunnlag = SamværskalkulatorDetaljer
