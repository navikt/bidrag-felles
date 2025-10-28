package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.annotation.JsonAlias
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

@Schema(description = "Informasjon om BPs løpende bidragssaker og nyeste manuelle beregning")
data class LøpendeBidragGrunnlag(
    val løpendeBidragListe: List<LøpendeBidrag>,
) : GrunnlagInnhold

data class LøpendeBidrag(
    val periode: ÅrMånedsperiode? = null,
    val saksnummer: Saksnummer,
    @JsonAlias("type")
    val stønadstype: Stønadstype,
    val løpendeBeløp: BigDecimal,
    val valutakode: String = "NOK",
    val samværsklasse: Samværsklasse,
    val beregnetBeløp: BigDecimal,
    val faktiskBeløp: BigDecimal,
    val gjelderBarn: Grunnlagsreferanse,
) : GrunnlagInnhold
