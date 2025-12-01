package no.nav.bidrag.commons.testdatagenerering.person;

import no.nav.bidrag.commons.testdatagenerering.adresse.Adressetilknytning;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static no.nav.bidrag.commons.testdatagenerering.person.FamilieBuilder.familie;
import static no.nav.bidrag.commons.testdatagenerering.person.ForeldreBuilder.foreldre;
import static no.nav.bidrag.commons.testdatagenerering.person.Kjonn.KVINNE;
import static no.nav.bidrag.commons.testdatagenerering.person.Kjonn.MANN;
import static no.nav.bidrag.commons.testdatagenerering.person.Relasjon.SAMME_ETTERNAVN;
import static no.nav.bidrag.commons.testdatagenerering.person.TestPersonBuilder.person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestPersonBuilderTest {

    @Test
    public void testPersonIdent() {
        TestPerson person = person()
                .opprett();

        assertThat(person, is(not(nullValue())));
        assertThat(person.getPersonIdent(), is(not(nullValue())));
        assertThat(person.getPersonIdent().length(), is(11));
    }

    @Test
    public void testKjonn() {
        assertThat(person().opprett().getKjonn(), is(oneOf(KVINNE, MANN)));
        assertThat(person().kjonn(KVINNE).opprett().getKjonn(), is(equalTo(KVINNE)));
        assertThat(person().kjonn(MANN).opprett().getKjonn(), is(equalTo(MANN)));
    }

    @Test
    public void testFodtDato() {
        assertThat(
                person().alder(10).opprett().getFodselsdato(),
                is(both(greaterThan(LocalDate.now().minusYears(11)))
                        .and(lessThanOrEqualTo(LocalDate.now().minusYears(10)))));
    }

    @Test
    public void testFornavn() {
        assertThat(
                person().opprett().getFornavn(),
                is(not(nullValue())));
        assertThat(
                person().fornavn("Ola").opprett().getFornavn(),
                is(equalTo("Ola")));
    }

    @Test
    public void testEtternavn() {
        assertThat(
                person().opprett().getEtternavn(),
                is(not(nullValue())));
        assertThat(
                person().etternavn("Nordmann").opprett().getEtternavn(),
                is(equalTo("Nordmann")));
    }

    @Test
    public void testForeldre() {
        TestPerson person = person()
                .fornavn("Ola")
                .etternavn("Nordmann")
                .med(foreldre()
                        .mor(p -> p
                                        .fornavn("Kari"),
                                SAMME_ETTERNAVN)
                        .far(p -> p
                                        .fornavn("Per"),
                                SAMME_ETTERNAVN))
                .opprett();

        TestPerson mor = person.getMor();
        assertThat(mor, is(not(nullValue())));
        assertThat(mor.getFornavn(), is(equalTo("Kari")));
        assertThat(mor.getEtternavn(), is(equalTo("Nordmann")));
        assertThat(mor.getBarn().get(0), is(person));

        TestPerson far = person.getFar();
        assertThat(far, is(not(nullValue())));
        assertThat(far.getFornavn(), is(equalTo("Per")));
        assertThat(far.getEtternavn(), is(equalTo("Nordmann")));
        assertThat(far.getBarn().get(0), is(person));
    }

    @Test
    public void testFamilieMedPartner() {
        TestPerson person = person()
                .kjonn(MANN)
                .fornavn("Ola")
                .etternavn("Nordmann")
                .med(familie()
                        .partner(p -> p
                                        .fornavn("Kari"),
                                SAMME_ETTERNAVN)
                        .barn(SAMME_ETTERNAVN))
                .opprett();

        TestPerson barn = person.getBarn().get(0);
        assertThat(barn.getEtternavn(), is(equalTo("Nordmann")));

        assertThat(barn.getFar(), is(person));
        assertThat(barn.getMor(), is(not(nullValue())));
        assertThat(barn.getMor().getFornavn(), is(equalTo("Kari")));
        assertThat(barn.getMor().getEtternavn(), is(equalTo("Nordmann")));
    }

    @Test
    public void testDefaultAdresse() {
        TestPerson person = person()
                .alder(24)
                .opprett();

        List<Adressetilknytning> historikk = person.getAdressehistorikk();
        assertThat("Person skal ha adressehistorikk",
                historikk,
                is(not(nullValue())));
        assertThat("Person mellom 18 og 23 kan ha flyttet ut."
                        + "Barn 24  og eldre har alltid flyttet ut og har derfor 2 innslag i adressehistorikken",
                historikk.size(),
                is(2));
        assertThat("Aktiv bostedsadresse er siste innslag i historikken",
                person.getBoadresse(),
                is(historikk.get(1).getAdresse()));
    }
}