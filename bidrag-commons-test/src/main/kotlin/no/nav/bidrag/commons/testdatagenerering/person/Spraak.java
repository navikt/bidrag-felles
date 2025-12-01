package no.nav.bidrag.commons.testdatagenerering.person;

import lombok.Data;
import lombok.NonNull;

@Data
public class Spraak {
    public static final Spraak NORSK = new Spraak("NO");
    public static final Spraak NORSK_BOKMAAL = new Spraak("NB");
    public static final Spraak NORSK_NYNORK = new Spraak("NN");
    public static final Spraak ENGELSK = new Spraak("EN");
    public static final Spraak TYSK = new Spraak("DE");

    @NonNull
    private final String spraakKode;
}
