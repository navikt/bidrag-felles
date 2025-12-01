package no.nav.bidrag.commons.testdatagenerering.person;

import java.time.LocalDate;

import static no.nav.bidrag.commons.testdatagenerering.RandomTestData.random;
import static no.nav.bidrag.commons.testdatagenerering.person.Kjonn.KVINNE;
import static no.nav.bidrag.commons.testdatagenerering.person.Kjonn.MANN;


public interface IdentType {
    String generer(LocalDate fodtDato, Kjonn kjonn);

    default String generer() {
        return generer(
                random().dateBetween(LocalDate.of(1900, 1, 1), LocalDate.now()),
                random().oneOf(KVINNE, MANN));
    }
}
