package no.nav.bidrag.commons.testdatagenerering.person;

import lombok.Data;

@Data
public class TestPersonIdent {
    private final String ident;
    private final boolean aktiv;
    private final boolean aktoerId;
}
