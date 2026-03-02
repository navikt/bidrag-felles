@file:Suppress("unused")

package no.nav.bidrag.domene.enums.sak

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class Arbeidsfordeling(
    private val beskrivelse: String,
    val behandlingstema: String?,
) {
    BBF("Barnebortføring", "ab0323"),
    EEN("Eierenhet", null),
    EFS("Ektefellesak", "ab0325"),
    FRS("Farskap", "ab0322"),
    INH("Settekontor", null),
    OPS("Oppfostringssak", "ab0324"),
}

@Schema(enumAsRef = true)
enum class ArbeidsfordelingV2(
    private val bisysKode: String,
    val behandlingstema: String?,
) {
    BARNEBORTFØRING("BBF", "ab0323"),
    EIERENHET("EEN", null),
    EKTEFELLLESAK("EFS", "ab0325"),
    FARSKAP("FRS", "ab0322"),
    SETTEKONTOR("INH", null),
    OPPFOSTRINGSSAK("OPS", "ab0324"),
    REISEKOSTNADSAK("RKS", "ab0129"),
}
