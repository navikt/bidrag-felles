package no.nav.bidrag.domene.enums.særligeutgifter

enum class Utgiftstype(val kategori: SærligeutgifterKategori) {
    KONFIRMASJONSAVGIFT(SærligeutgifterKategori.KONFIRMASJON),
    KONFIRMASJONSLEIR(SærligeutgifterKategori.KONFIRMASJON),
    SELSKAP(SærligeutgifterKategori.KONFIRMASJON),
    KLÆR(SærligeutgifterKategori.KONFIRMASJON),
    REISEUTGIFT(SærligeutgifterKategori.KONFIRMASJON),

    TANNREGULERING(SærligeutgifterKategori.TANNREGULERING),
    OPTIKK(SærligeutgifterKategori.OPTIKK),
}
