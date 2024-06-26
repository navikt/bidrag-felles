package no.nav.bidrag.domene.enums.særbidrag

enum class Utgiftstype(
    val kategori: SærbidragKategori,
) {
    KONFIRMASJONSAVGIFT(SærbidragKategori.KONFIRMASJON),
    KONFIRMASJONSLEIR(SærbidragKategori.KONFIRMASJON),
    SELSKAP(SærbidragKategori.KONFIRMASJON),
    KLÆR(SærbidragKategori.KONFIRMASJON),
    REISEUTGIFT(SærbidragKategori.KONFIRMASJON),

    TANNREGULERING(SærbidragKategori.TANNREGULERING),
    OPTIKK(SærbidragKategori.OPTIKK),
}
