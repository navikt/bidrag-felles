package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.person.Bostatuskode
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.time.LocalDateTime

data class InnhentetHusstandsmedlem(
    override val periode: Datoperiode,
    override val grunnlag: HusstandsmedlemPDL,
    override val hentetTidspunkt: LocalDateTime,
) : InnhentetGrunnlagInnhold<InnhentetHusstandsmedlem.HusstandsmedlemPDL> {
    data class HusstandsmedlemPDL(
        @Schema(description = "Referanse til person som er husstandsmedlem")
        val relatertPerson: Grunnlagsreferanse?,
        val erBarnAvBmBp: Boolean,
    )
}

@Schema(description = "Bostatus for person")
data class BostatusPeriode(
    override val periode: ÅrMånedsperiode,
    val bostatus: Bostatuskode,
    override val manueltRegistrert: Boolean,
) : GrunnlagPeriodeInnhold
