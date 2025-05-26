package no.nav.bidrag.transport.samhandler

enum class Områdekode(
    val tssOmrådekode: String,
    val visningsnavn: String,
) {
    UTENRIKSSTASJON("0050", "Utenriksstasjon"),
    ADVOKAT("0060", "Advokat"),
    ARBEIDSGIVER("0070", "Arbeidsgiver"),
    REELL_MOTTAKER("0080", "Reell mottaker"),
    UTENLANDSK_PERSON("0260", "Utenlandsk person"),
    UTENLANDSK_FOGD("0270", "Utenlandsk fogd"),
    SOSIALKONTO("0280", "Sosialkontor"),
    BARNEVERNSINSTITUSJON("0290", "Barnevernsinstitusjon"),
    ;

    companion object {
        fun fraTssOmrådekode(tssOmrådekode: String): Områdekode? = entries.find { it.tssOmrådekode == tssOmrådekode }

        fun fraVisningsnavn(visningsnavn: String): Områdekode? = entries.find { it.visningsnavn == visningsnavn }
    }
}
