package no.nav.bidrag.transport.behandling.beregning.felles

import no.nav.bidrag.domene.enums.behandling.Behandlingstatus
import no.nav.bidrag.domene.enums.behandling.Behandlingstype
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.domene.enums.rolle.SøktAvType
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.transport.behandling.hendelse.BehandlingStatusType
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
    val innkreving: Boolean,
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
    val innkreving: Boolean,
    val barnListe: List<Barn>,
)

data class Barn(
    val personident: String,
)

data class OpprettSøknadResponse(
    val søknadsid: String,
)

data class HentSøknadRequest(
    val søknadsid: String,
)

data class HentSøknadResponse(
    val søknad: HentSøknad,
)

data class HentSøknad(
    val søknadsid: String,
    val blankettid: String,
    val søknadMottattDato: LocalDate,
    val søknadFomDato: LocalDate? = null,
    val stønadstype: Stønadstype,
    val behandlerenhet: String? = null,
    val saksnummer: String,
    val behandlingsid: String? = null,
    val behandlingStatusType: BehandlingStatusType,
    val hentSøknadslinjerListe: List<HentSøknadslinje> = emptyList(),
)

data class HentSøknadslinje(
    val søknadslinjeid: String,
    val personidentBarn: String,
    val behandlingstatus: Behandlingstatus,
)
