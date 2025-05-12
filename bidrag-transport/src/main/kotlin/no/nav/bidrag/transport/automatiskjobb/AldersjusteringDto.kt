package no.nav.bidrag.transport.automatiskjobb

import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.sak.Stønadsid
import no.nav.bidrag.transport.behandling.vedtak.request.OpprettVedtakRequestDto
import java.time.Year

data class HentAldersjusteringStatusRequest(
    val saksnummer: Saksnummer,
    val barnListe: List<Personident>,
    val år: Int = Year.now().value,
)

data class AldersjusteringAldersjustertResultat(
    val vedtaksid: Int,
    val stønadsid: Stønadsid,
    val vedtak: OpprettVedtakRequestDto? = null,
) : AldersjusteringResultat(true) {
    constructor() : this(
        0,
        Stønadsid(
            Stønadstype.BIDRAG,
            kravhaver = Personident(""),
            skyldner = Personident(""),
            sak = Saksnummer(""),
        ),
    )
}

data class AldersjusteringIkkeAldersjustertResultat(
    val stønadsid: Stønadsid,
    val begrunnelse: String,
    val aldersjusteresManuelt: Boolean = false,
) : AldersjusteringResultat(false) {
    constructor() : this(
        Stønadsid(
            Stønadstype.BIDRAG,
            kravhaver = Personident(""),
            skyldner = Personident(""),
            sak = Saksnummer(""),
        ),
        "",
    )
}

abstract class AldersjusteringResultat(
    val aldersjustert: Boolean,
) {
    constructor() : this(false)
}

data class AldersjusteringResultatlisteResponse(
    val resultatListe: List<AldersjusteringResultat>,
) {
    constructor() : this(emptyList())
}
