package no.nav.bidrag.commons.testdatagenerering.navn;

import lombok.Data;
import no.nav.bidrag.commons.testdatagenerering.PeriodisertTestData;

import java.time.LocalDate;

@Data
public class TestNavn implements PeriodisertTestData {
    private final String fornavn;
    private final String etternavn;
    private final LocalDate periodeFra;
    private final LocalDate periodeTil;

    public String getSammensatt() {
        return getEtternavn() + ", " + getFornavn();
    }
}
