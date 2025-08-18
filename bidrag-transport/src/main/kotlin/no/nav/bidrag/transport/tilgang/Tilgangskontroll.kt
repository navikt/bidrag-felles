package no.nav.bidrag.transport.tilgang

import io.swagger.v3.oas.annotations.media.Schema

data class TilgangskontrollResponse(
    @param:Schema(
        description =
            "Indikerer om brukeren har tilgang. " +
                "Hvis tilgang er avslått, vil detaljer inneholde mer informasjon.",
    )
    val harTilgang: Boolean,
    @param:Schema(
        description =
            "Liste over detaljer om tilgangsbeslutninger, inkludert opprinnelse og begrunnelse. " +
                "Vil kun settes om tilgang avslås.",
    )
    val detaljer: List<TilgangskontrollResponseDetaljer> = emptyList(),
)

data class TilgangskontrollResponseDetaljer(
    val harTilgang: Boolean,
    val begrunnelse: String,
    val opprinnelseTilgangsbeslutning: OpprinnelseTilgangsbeslutning,
)

enum class OpprinnelseTilgangsbeslutning {
    TILGANGSMASKIN,
    GRAPH,
    BIDRAG_SAK_PIP,
    BIDRAG_TILGANGSKONTROLL,
}
