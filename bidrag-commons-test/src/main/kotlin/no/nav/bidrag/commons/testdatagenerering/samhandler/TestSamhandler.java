package no.nav.bidrag.commons.testdatagenerering.samhandler;

import lombok.Data;

@Data
public class TestSamhandler {
    private final String samhandlerIdent;
    private final String shIdent;
    private final SamhandlerIdentType identType;
    private final String navn;
    private final Samhandlertype type;
    private final SamhandlerFagomraade fagomraade;
}
