package no.nav.bidrag.transport.sak

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.sak.Bidragssakstatus
import no.nav.bidrag.domene.enums.sak.Sakskategori
import no.nav.bidrag.domene.organisasjon.Enhetsnummer
import no.nav.bidrag.domene.sak.Saksnummer
import java.time.LocalDate

@Schema(description = "Metadata for en bidragssak")
data class BidragssakDto(
    @Schema(description = "Eierfogd for bidragssaken")
    val eierfogd: Enhetsnummer,
    @Schema(description = "Saksnummeret til bidragssaken")
    val saksnummer: Saksnummer,
    @Schema(description = "Saksstatus til bidragssaken")
    val saksstatus: Bidragssakstatus,
    @Schema(description = "Kategorikode: 'N' eller 'U'")
    val kategori: Sakskategori,
    @Schema(description = "Om saken omhandler paragraf 19")
    val erParagraf19: Boolean = false,
    @Schema(description = "Om saken inneholder personer med diskresjonskode")
    val begrensetTilgang: Boolean = false,
    val opprettetDato: LocalDate,
    val levdeAdskilt: Boolean,
    @Schema(description = "Hvor vidt en av partene i saken er ukjent")
    val ukjentPart: Boolean,
    val vedtakssperre: Boolean = false,
    val avsluttet: Boolean = false,
    @Schema(description = "Rollene som saken inneholder")
    val roller: List<RolleDto> = emptyList(),
)

data class SamhandlerSakerDto(
    val antallSaker: Int,
    val saksnummere: List<String>,
)

data class SamhandlerSakerRequestDto(
    val samhandlerId: String,
)
