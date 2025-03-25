package no.nav.bidrag.domene.sak

import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.ident.Personident

data class Stønadsid(
    val gjelderBarn: Personident,
    val type: Stønadstype,
    val kravhaver: Personident,
    val sak: Saksnummer,
)
