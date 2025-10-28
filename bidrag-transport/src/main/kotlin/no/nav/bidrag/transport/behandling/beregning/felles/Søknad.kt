package no.nav.bidrag.transport.behandling.beregning.felles

import no.nav.bidrag.domene.enums.behandling.Behandlingstatus
import no.nav.bidrag.domene.enums.behandling.Behandlingstema
import no.nav.bidrag.domene.enums.behandling.Behandlingstype
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.domene.enums.rolle.SøktAvType
import no.nav.bidrag.transport.behandling.hendelse.BehandlingStatusType
import java.math.BigDecimal
import java.time.LocalDate

data class FeilregistrerSøknadRequest(
    val søknadsid: Long,
)

data class FeilregistrerSøknadsBarnRequest(
    val søknadsid: Long,
    val personidentBarn: String,
)

data class HentBPsÅpneSøknaderRequest(
    val personidentBP: String,
)

data class HentBPsÅpneSøknaderResponse(
    val åpneSøknader: List<ÅpenSøknadDto> = emptyList(),
)

data class ÅpenSøknadDto(
    val søknadsid: Long,
    val behandlingstype: Behandlingstype,
    val saksnummer: String,
    val behandlingstema: Behandlingstema,
    val innkreving: Boolean,
    val behandlingsid: Long? = null,
    val søknadMottattDato: LocalDate,
    val søknadFomDato: LocalDate? = null,
    val søktAvType: SøktAvType,
    val referertSøknadsid: Long? = null,
    val referertBehandlingsid: Long? = null,
    val referertVedtaksid: Int? = null,
    val partISøknadListe: List<PartISøknad> = emptyList(),
)

data class PartISøknad(
    val personident: String? = null,
    val rolletype: Rolletype,
    val innbetaltBeløp: BigDecimal? = BigDecimal.ZERO,
    val gebyr: Boolean = false,
)

data class LeggTilBarnIFFSøknadRequest(
    val søknadsid: Long,
    val personidentBarn: String,
)

data class OppdaterBehandlerenhetRequest(
    val behandlerenhet: String,
    val søknadsid: Long,
)

data class OppdaterBehandlingsidRequest(
    val eksisterendeBehandlingsid: Long? = null,
    val nyBehandlingsid: Long,
    val søknadsid: Long,
)

data class OpprettSøknadRequest(
    val saksnummer: String,
    val behandlingstema: Behandlingstema,
    val behandlingsid: Long? = null,
    val enhet: String,
    val søknadFomDato: LocalDate,
    val innkreving: Boolean,
    val barnListe: List<Barn>,
)

data class Barn(
    val personident: String,
)

data class OpprettSøknadResponse(
    val søknadsid: Long,
)

data class HentSøknadRequest(
    val søknadsid: Long,
)

data class HentSøknadResponse(
    val søknad: HentSøknad,
)

data class HentSøknad(
    val søknadsid: Long,
    val søknadMottattDato: LocalDate,
    val søknadFomDato: LocalDate? = null,
    val behandlingstema: Behandlingstema,
    val behandlerenhet: String? = null,
    val saksnummer: String,
    val behandlingsid: Long? = null,
    val behandlingStatusType: BehandlingStatusType,
    val hentSøknadslinjerListe: List<HentSøknadslinje> = emptyList(),
)

data class HentSøknadslinje(
    val personidentBarn: String,
    val behandlingstatus: Behandlingstatus,
)
