package no.nav.bidrag.domene.enums.særbidrag

enum class Utgiftstype(
    val kategori: Særbidragskategori,
) {
    KONFIRMASJONSAVGIFT(Særbidragskategori.KONFIRMASJON),
    KONFIRMASJONSLEIR(Særbidragskategori.KONFIRMASJON),
    SELSKAP(Særbidragskategori.KONFIRMASJON),
    KLÆR(Særbidragskategori.KONFIRMASJON),
    REISEUTGIFT(Særbidragskategori.KONFIRMASJON),

    TANNREGULERING(Særbidragskategori.TANNREGULERING),
    OPTIKK(Særbidragskategori.OPTIKK),
}
