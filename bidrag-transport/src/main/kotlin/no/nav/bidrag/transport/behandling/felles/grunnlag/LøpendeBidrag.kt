package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.annotation.JsonAlias
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.enums.sak.Sakskategori
import no.nav.bidrag.domene.enums.samhandler.Valutakode
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

//TODO Bytte ut med LøpendeBidragPeriode?
@Schema(description = "Informasjon om BPs løpende bidragssaker og nyeste manuelle beregning")
data class LøpendeBidragGrunnlag(
    val løpendeBidragListe: List<LøpendeBidrag>,
) : GrunnlagInnhold

data class LøpendeBidrag(
    val saksnummer: Saksnummer,
    @JsonAlias("stønadstype")
    val type: Stønadstype,
    val løpendeBeløp: BigDecimal,
    //TODO Bytte ut String med Valutakode
    val valutakode: String = "NOK",
    //TODO Gjøre nullable
    val samværsklasse: Samværsklasse,
    val beregnetBeløp: BigDecimal,
    val faktiskBeløp: BigDecimal,
    @Schema(description = "Referanse til barnet det løpende bidraget gjelder for")
    val gjelderBarn: Grunnlagsreferanse,
) : GrunnlagInnhold

@Schema(description = "Løpende bidrag periodisert")
data class LøpendeBidragPeriode(
    override val periode: ÅrMånedsperiode,
    val saksnummer: Saksnummer,
    val stønadstype: Stønadstype,
    val løpendeBeløp: BigDecimal,
    val valutakode: Valutakode = Valutakode.NOK,
    val samværsklasse: Samværsklasse? = null,
    val beregnetBeløp: BigDecimal,
    val faktiskBeløp: BigDecimal,
    val sakskategori: Sakskategori = Sakskategori.N,
    override val manueltRegistrert: Boolean = false,
) : GrunnlagPeriodeInnhold
