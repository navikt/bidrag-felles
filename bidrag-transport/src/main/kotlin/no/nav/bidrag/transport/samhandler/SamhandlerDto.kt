package no.nav.bidrag.transport.samhandler

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.ident.SamhandlerId

data class SamhandlerDto(
    val tssId: SamhandlerId,
    @Schema(description = "Navn på samhandler")
    val navn: String?,

    @Schema(description = "Offentlig id for samhandlere.")
    val offentligId: String? = null,

    @Schema(description = "Type offentlig id. F.eks ORG for norske organisasjonsnummere.")
    val offentligIdType: String? = null,

    @Schema(description = "Samhandlerens adresse.")
    val adresse: AdresseDto? = null,

    @Schema(description = "Samhandlerens kontonummer. Kontonummer er ikke inkludert i søkeresultat, kun i oppslag.")
    val kontonummer: KontonummerDto? = null,
)
