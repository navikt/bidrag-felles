package no.nav.bidrag.commons.testdatagenerering;

import java.time.LocalDate;

public interface PeriodisertTestData {
    LocalDate getPeriodeFra();

    LocalDate getPeriodeTil();

    default boolean overlapperMed(LocalDate dag) {
        return !getPeriodeFra().isAfter(dag)
                && (getPeriodeTil() == null || getPeriodeTil().isAfter(dag));
    }

    default boolean overlapperMedIdag() {
        return overlapperMed(LocalDate.now());
    }
}
