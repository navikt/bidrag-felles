
package no.nav.bidrag.domene.enums.regnskap

import no.nav.bidrag.domene.enums.vedtak.Engangsbeløptype
import no.nav.bidrag.domene.enums.vedtak.Stønadstype

enum class Transaksjonskode(val korreksjonskode: String?, val negativtBeløp: Boolean) {
    A1("A3", false), // Bidragsforskudd
    A3(null, true),
    B1("B3", false), // Underholdsbidrag (m/u tilleggsbidrag)
    B3(null, true),
    D1("D3", false), // 18årsbidrag
    D3(null, true),
    E1("E3", false), // Bidrag til særlige utgifter (særtilskudd)
    E3(null, true),
    F1("F3", false), // Ektefellebidrag
    F3(null, true),
    G1("G3", false), // Gebyr
    G3(null, true),
    H1("H3", false), // Tilbakekreving
    H3(null, true),
    I1(null, true), // Motregning
    K1(null, true), // Ettergivelse
    K2(null, true), // Direkte oppgjør (innbetalt beløp)
    K3(null, true), // Tilbakekreving ettergivelse
    ;

    companion object {
        fun hentTransaksjonskodeForType(type: String): Transaksjonskode {
            return when (type) {
                Stønadstype.FORSKUDD.name -> A1
                Stønadstype.BIDRAG.name -> B1
                Stønadstype.OPPFOSTRINGSBIDRAG.name -> B1
                Stønadstype.BIDRAG18AAR.name -> D1
                Stønadstype.EKTEFELLEBIDRAG.name -> F1
                Stønadstype.MOTREGNING.name -> I1
                Engangsbeløptype.SAERTILSKUDD.name -> E1
                Engangsbeløptype.SÆRTILSKUDD.name -> E1
                Engangsbeløptype.GEBYR_MOTTAKER.name -> G1
                Engangsbeløptype.GEBYR_SKYLDNER.name -> G1
                Engangsbeløptype.TILBAKEKREVING.name -> H1
                Engangsbeløptype.ETTERGIVELSE.name -> K1
                Engangsbeløptype.DIREKTE_OPPGJOR.name -> K2
                Engangsbeløptype.DIREKTE_OPPGJØR.name -> K2
                Engangsbeløptype.ETTERGIVELSE_TILBAKEKREVING.name -> K3
                else -> throw IllegalStateException("Ugyldig type for transaksjonskode funnet!")
            }
        }
    }
}
