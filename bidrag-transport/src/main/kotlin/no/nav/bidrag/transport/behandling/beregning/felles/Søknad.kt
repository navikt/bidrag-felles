package no.nav.bidrag.transport.behandling.beregning.felles

import no.nav.bidrag.domene.enums.behandling.Behandlingstype
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.domene.enums.rolle.SøktAvType
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import java.math.BigDecimal
import java.time.LocalDate

data class FeilregistrerSøknadRequest(
    val søknadsid: String,
)

data class HentBPsÅpneSøknaderRequest(
    val personidentBP: String,
)

data class HentBPsÅpneSøknaderResponse(
    val åpneSøknader: List<ÅpenSøknadDto> = emptyList(),
)

data class ÅpenSøknadDto(
    val behandlingstype: Behandlingstype,
    val saksnummer: String,
    val søknadsid: String,
    val stønadstype: Stønadstype,
    val behandlingsid: String?,
    val søknadMottattDato: LocalDate,
    val søknadFomDato: LocalDate?,
    val søktAvType: SøktAvType,
    val partISøknadListe: List<PartISøknad> = emptyList(),
)

data class PartISøknad(
    val personident: String?,
    val rolletype: Rolletype,
    val innbetaltBeløp: BigDecimal? = BigDecimal.ZERO,
    val gebyr: Boolean = false,
)

data class LeggTilBarnIFFSøknadRequest(
    val søknadsid: String,
    val personidentBarn: String,
    val innkreving: Boolean,
)

data class OppdaterBehandlerenhetRequest(
    val behandlerenhet: String,
    val søknadsid: String,
)

data class OppdaterBehandlingsidRequest(
    val eksisterendeBehandlingsid: String? = null,
    val nyBehandlingsid: String,
    val søknadsid: String,
)

data class OpprettSøknadRequest(
    val saksnummer: String,
    val stønadstype: Stønadstype,
    val behandlingsid: String?,
    val enhet: String,
    val søknadFomDato: LocalDate,
    val barnListe: List<Barn>,
)

data class Barn(
    val personident: String,
    val innkreving: Boolean,
)

data class OpprettSøknaderResponse(
    val søknadsid: String,
)
