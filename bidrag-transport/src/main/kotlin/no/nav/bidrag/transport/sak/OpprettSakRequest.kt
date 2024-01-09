package no.nav.bidrag.transport.sak

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.sak.Konvensjon
import no.nav.bidrag.domene.enums.sak.Sakskategori
import no.nav.bidrag.domene.land.Landkode
import no.nav.bidrag.domene.organisasjon.Enhetsnummer
import java.time.LocalDate

class OpprettSakRequest(
    @Schema(description = "Sakens eierfogd (enhetsnummeret som får tilgang til saken.")
    val eierfogd: Enhetsnummer,
    val kategori: Sakskategori = Sakskategori.N,
    val ansatt: Boolean = false,
    val inhabilitet: Boolean = false,
    val levdeAdskilt: Boolean = false,
    val paragraf19: Boolean = false,
    @Schema(description = "Kovensjonskode tilsvarende kodene i T_KODE_KONVENSJON.")
    val konvensjon: Konvensjon? = null,
    val konvensjonsdato: LocalDate? = null,
    val ffuReferansenr: String? = null,
    val land: Landkode? = null,
    val roller: Set<RolleDto> = setOf(),
)
