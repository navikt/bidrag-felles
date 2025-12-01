package no.nav.bidrag.commons.testdatagenerering.person;

import lombok.Data;
import lombok.ToString;
import no.nav.bidrag.commons.testdatagenerering.PeriodisertTestData;
import no.nav.bidrag.commons.testdatagenerering.adresse.Adressetilknytning;
import no.nav.bidrag.commons.testdatagenerering.adresse.TestAdresse;
import no.nav.bidrag.commons.testdatagenerering.navn.TestNavn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;
import static no.nav.bidrag.commons.testdatagenerering.adresse.Adressetype.*;

@Data
@ToString(exclude = { "mor", "far" })
public class TestPerson {
    private final List<TestPersonIdent> personidenter;
    private final Kjonn kjonn;
    private final LocalDate fodselsdato;
    private final LocalDate dodDato;
    private final List<TestNavn> navnhistorikk;

    private final TestPerson mor;
    private final TestPerson far;
    private final List<TestPerson> barn = new ArrayList<>();

    private final Diskresjon diskresjon;
    private final Spraak spraak;

    private final List<Adressetilknytning> adressehistorikk;

    public TestPerson(
            List<TestPersonIdent> personidenter,
            Kjonn kjonn,
            LocalDate fodselsdato,
            LocalDate dodDato,
            List<TestNavn> navnhistorikk,
            TestPerson mor,
            TestPerson far,
            Diskresjon diskresjon,
            Spraak spraak,
            List<Adressetilknytning> adressehistorikk) {

        this.personidenter = personidenter;
        this.kjonn = kjonn;
        this.fodselsdato = fodselsdato;
        this.dodDato = dodDato;
        this.navnhistorikk = unmodifiableList(new ArrayList<>(navnhistorikk));
        this.mor = mor;
        this.far = far;
        this.diskresjon = diskresjon;
        this.spraak = spraak;
        this.adressehistorikk = unmodifiableList(new ArrayList<>(adressehistorikk));

        if (mor != null) {
            mor.getBarn().add(this);
        }
        if (far != null) {
            far.getBarn().add(this);
        }
    }

    public String getPersonIdent() {
        return personidenter.stream()
                .filter(i -> i.isAktiv() && !i.isAktoerId())
                .findFirst()
                .map(TestPersonIdent::getIdent)
                .orElse(null);
    }

    public String getAktoerid() {
        return personidenter.stream()
                .filter(i -> i.isAktiv() && i.isAktoerId())
                .findFirst()
                .map(TestPersonIdent::getIdent)
                .orElse(null);
    }

    public String getFornavn() {
        return getNavn().map(TestNavn::getFornavn).orElse(null);
    }

    public String getEtternavn() {
        return getNavn().map(TestNavn::getEtternavn).orElse(null);
    }

    public String getSammensattNavn() {
        return getFornavn() + " " + getEtternavn();
    }

    public Optional<TestNavn> getNavn() {
        return navnhistorikk.stream()
                .filter(PeriodisertTestData::overlapperMedIdag)
                .findFirst();
    }

    public Optional<TestPerson> mor() {
        return ofNullable(getMor());
    }

    public Optional<TestPerson> far() {
        return ofNullable(getFar());
    }

    public boolean harBarn() {
        return !barn.isEmpty();
    }

    public TestPerson barn(int barnNr) {
        return barn.get(barnNr);
    }

    public TestPerson denAndreForelderen(int barnNr) {
        return denAndreForelderen(this, barn(barnNr));
    }

    public static TestPerson denAndreForelderen(TestPerson forelder1, TestPerson barn) {
        return forelder1.equals(barn.getFar())
                ? barn.getMor()
                : barn.getFar();
    }

    public TestAdresse getPostadresse() {
        LocalDate today = LocalDate.now();
        return getAdressehistorikk()
                .stream()
                .filter(a -> KONTAKTADRESSE.equals(a.getType()))
                .filter(a -> a.overlapperMed(today))
                .map(Adressetilknytning::getAdresse)
                .findFirst()
                .orElse(null);
    }

    public TestAdresse getBoadresse() {
        LocalDate today = LocalDate.now();
        return getAdressehistorikk()
                .stream()
                .filter(a -> BOSTEDSADRESSE.equals(a.getType()) || OPPHOLDSADRESSE.equals(a.getType()))
                .filter(a -> a.overlapperMed(today))
                .map(Adressetilknytning::getAdresse)
                .findFirst()
                .orElse(null);
    }
}
