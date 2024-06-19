package no.nav.bidrag.domene.enums.særligeutgifter

import no.nav.bidrag.domene.enums.vedtak.Engangsbeløptype

enum class Utgiftstype(val kategori: Engangsbeløptype) {
    KONFIRMASJONSAVGIFT(Engangsbeløptype.SÆRTILSKUDD_KONFIRMASJON),
    KONFIRMASJONSLEIR(Engangsbeløptype.SÆRTILSKUDD_KONFIRMASJON),
    SELSKAP(Engangsbeløptype.SÆRTILSKUDD_KONFIRMASJON),
    KLÆR(Engangsbeløptype.SÆRTILSKUDD_KONFIRMASJON),
    REISEUTGIFT(Engangsbeløptype.SÆRTILSKUDD_KONFIRMASJON),

    TANNREGULERING(Engangsbeløptype.SÆRTILSKUDD_TANNREGULERING),
    OPTIKK(Engangsbeløptype.SÆRTILSKUDD_OPTIKK),
}
