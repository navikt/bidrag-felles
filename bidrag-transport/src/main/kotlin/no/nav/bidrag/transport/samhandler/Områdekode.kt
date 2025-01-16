package no.nav.bidrag.transport.samhandler

enum class Områdekode(
    val tssOmrådekode: String,
) {
    UTENRIKSSTASJON("0050"),
    ADVOKAT("0060"),
    ARBEIDSGIVER("0070"),
    REELL_MOTTAKER("0080"),
    UTENLANDSK_PERSON("0260"),
    UTENLANDSK_FOGD("0270"),
    SOSIALKONTO("0280"),
    BARNEVERNSINSTITUSJON("0290"),
    ;

    companion object {
        fun fraTssOmrådekode(tssOmrådekode: String): Områdekode? = entries.find { it.tssOmrådekode == tssOmrådekode }
    }
}
