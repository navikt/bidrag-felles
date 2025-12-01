package no.nav.bidrag.commons.testdatagenerering.adresse;

import lombok.Data;
import no.nav.bidrag.commons.testdatagenerering.PeriodisertTestData;

import java.time.LocalDate;

@Data
public class Adressetilknytning implements PeriodisertTestData {
    private final TestAdresse adresse;
    private final Adressetype type;
    private final LocalDate periodeFra;
    private final LocalDate periodeTil;
}
