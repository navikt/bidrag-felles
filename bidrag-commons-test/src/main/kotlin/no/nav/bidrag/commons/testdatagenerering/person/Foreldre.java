package no.nav.bidrag.commons.testdatagenerering.person;

import lombok.Data;

@Data
public class Foreldre {
    private final TestPerson mor;
    private final TestPerson far;
}
