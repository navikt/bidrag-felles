package no.nav.bidrag.commons.testdatagenerering.person;

import lombok.Data;
import no.nav.bidrag.commons.testdatagenerering.RandomTestData;
import no.nav.bidrag.commons.testdatagenerering.adresse.Adressetilknytning;
import no.nav.bidrag.commons.testdatagenerering.adresse.AdressetilknytningBuilder;
import no.nav.bidrag.commons.testdatagenerering.adresse.TestAdresse;
import no.nav.bidrag.commons.testdatagenerering.adresse.TestAdresseBuilder;
import no.nav.bidrag.commons.testdatagenerering.navn.NavnBuilder;
import no.nav.bidrag.commons.testdatagenerering.navn.TestNavn;
import no.nav.bidrag.commons.testdatagenerering.time.DateBuilder;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static no.nav.bidrag.commons.testdatagenerering.RandomTestData.random;
import static no.nav.bidrag.commons.testdatagenerering.adresse.Adressetype.BOSTEDSADRESSE;
import static no.nav.bidrag.commons.testdatagenerering.adresse.Adressetype.KONTAKTADRESSE;
import static no.nav.bidrag.commons.testdatagenerering.adresse.TestAdresseBuilder.adresse;
import static no.nav.bidrag.commons.testdatagenerering.time.DateUtils.parseDate;

public class TestPersonBuilder {
    private List<IdentType> identtyper = asList(IdentTyper.FNR);
    private int antallAktoerIder = 1;
    private Kjonn kjonn;
    private int alderMin = 25;
    private int alderMax = 80;
    private LocalDate fodtDato;
    private LocalDate dodDato;
    private final List<NavnBuilder> navnhistorikk = new ArrayList<>();

    private ForeldreBuilder foreldreBuilder;
    private TestPerson mor;
    private TestPerson far;
    private List<FamilieBuilder> familier = new ArrayList<>();

    private Diskresjon diskresjon;
    private Spraak spraak;

    private List<AdressetilknytningBuilder> adresser = new ArrayList<>();

    public static TestPersonBuilder person() {
        return new TestPersonBuilder();
    }

    public TestPersonBuilder identType(IdentType... identtyper) {
        this.identtyper = asList(identtyper);
        return this;
    }

    public TestPersonBuilder kjonn(Kjonn kjonn) {
        this.kjonn = kjonn;
        return this;
    }

    public TestPersonBuilder fodtDato(LocalDate fodtDato) {
        this.fodtDato = fodtDato;
        return this;
    }

    public TestPersonBuilder fodt(DateBuilder dateBuilder) {
        return fodtDato(dateBuilder.get());
    }

    public TestPersonBuilder fodtDato(String fodtDatoStr) {
        this.fodtDato = parseDate(fodtDatoStr);
        return this;
    }

    public TestPersonBuilder alder(int alderMin, int alderMax) {
        this.fodtDato = null;
        this.alderMin = alderMin;
        this.alderMax = alderMax;
        return this;
    }

    public TestPersonBuilder alder(int alder) {
        return alder(alder, alder);
    }

    public TestPersonBuilder barnAlder() {
        return alder(0, 18);
    }

    public TestPersonBuilder voksenAlder() {
        return alder(25, 40);
    }

    public TestPersonBuilder besteforeldreAlder() {
        return alder(45, 60);
    }

    public TestPersonBuilder dodDato(LocalDate dodDato) {
        this.dodDato = dodDato;
        return this;
    }

    public TestPersonBuilder erDod() {
        return dodDato(LocalDate.now().minusDays(3));
    }

    public TestPersonBuilder fornavn(String fornavn) {
        sisteNavn().fornavn(fornavn);
        return this;
    }

    public TestPersonBuilder etternavn(String etternavn) {
        sisteNavn().etternavn(etternavn);
        return this;
    }

    private NavnBuilder sisteNavn() {
        if (navnhistorikk.isEmpty()) {
            navnhistorikk.add(new NavnBuilder());
        }
        return navnhistorikk.get(navnhistorikk.size() - 1);
    }

    public TestPersonBuilder med(NavnBuilder navn) {
        navnhistorikk.add(navn);
        return this;
    }

    public TestPersonBuilder mor(TestPerson mor, Relasjon... relasjoner) {
        this.mor = mor;
        return relasjonTil(mor, relasjoner);
    }

    public TestPersonBuilder far(TestPerson far, Relasjon... relasjoner) {
        this.far = far;
        return relasjonTil(far, relasjoner);
    }

    public TestPersonBuilder med(ForeldreBuilder foreldre) {
        this.foreldreBuilder = foreldre;
        return this;
    }

    public TestPersonBuilder med(FamilieBuilder familie) {
        this.familier.add(familie);
        return this;
    }

    public TestPersonBuilder relasjonTil(TestPerson person, Relasjon... relasjoner) {
        for (Relasjon relasjon : relasjoner) {
            relasjon.relater(person, this);
        }
        return this;
    }

    public TestPersonBuilder diskresjon(Diskresjon diskresjon) {
        this.diskresjon = diskresjon;
        return this;
    }

    public TestPersonBuilder spraak(Spraak spraak) {
        this.spraak = spraak;
        return this;
    }

    public TestPersonBuilder boadresse(TestAdresse boadresse) {
        return med(new AdressetilknytningBuilder(boadresse, null, BOSTEDSADRESSE));
    }

    public TestPersonBuilder boadresse(TestAdresseBuilder adresseBuilder) {
        return med(new AdressetilknytningBuilder(null, adresseBuilder, BOSTEDSADRESSE));
    }

    public TestPersonBuilder postadresse(TestAdresse boadresse) {
        return med(new AdressetilknytningBuilder(boadresse, null, KONTAKTADRESSE));
    }

    public TestPersonBuilder postadresse(TestAdresseBuilder adresseBuilder) {
        return med(new AdressetilknytningBuilder(null, adresseBuilder, KONTAKTADRESSE));
    }

    public TestPersonBuilder med(AdressetilknytningBuilder adressetilknytningBuilder) {
        this.adresser.add(adressetilknytningBuilder);
        return this;
    }

    public TestPerson opprett() {
        LocalDate fodtDato = beregnFodtDato();
        Kjonn kjonn = this.kjonn != null
                ? this.kjonn
                : random().oneOf(Kjonn.class);

        List<TestPersonIdent> personidenter = opprettIdenter(kjonn, fodtDato);

        List<TestNavn> navnhistorikk = opprettNavnhistorikk(kjonn, fodtDato);

        List<Adressetilknytning> adressehistorikk = opprettAdressehistorikk(fodtDato);

        Foreldre foreldre = finnForeldre(
                new TestPerson(
                        personidenter,
                        kjonn,
                        fodtDato,
                        dodDato,
                        navnhistorikk,
                        mor,
                        far,
                        diskresjon,
                        spraak,
                        adressehistorikk));

        TestPerson person = new TestPerson(
                personidenter,
                kjonn,
                fodtDato,
                dodDato,
                navnhistorikk,
                foreldre.getMor(),
                foreldre.getFar(),
                diskresjon,
                spraak,
                adressehistorikk);

        for (FamilieBuilder familie : familier) {
            familie.get(person);
        }
        return person;
    }

    private List<TestPersonIdent> opprettIdenter(Kjonn kjonn, LocalDate fodtDato) {
        ArrayList<TestPersonIdent> identer = new ArrayList<>();
        Iterator<IdentType> it = identtyper.iterator();
        while (it.hasNext()) {
            identer.add(new TestPersonIdent(it.next().generer(fodtDato, kjonn), !it.hasNext(), false));
        }
        for (int i = 0; i < antallAktoerIder; i++) {
            identer.add(new TestPersonIdent(Long.toString(100000000000l + random().nextLong(200000000000L)), i == 0, true));
        }
        return identer;
    }

    private List<TestNavn> opprettNavnhistorikk(Kjonn kjonn, LocalDate fraDato) {
        if (navnhistorikk.isEmpty()) {
            return NavnBuilder.lagNavnhistorikk(List.of(new NavnBuilder()), kjonn, fraDato);
        }
        return NavnBuilder.lagNavnhistorikk(navnhistorikk, kjonn, fraDato);
    }

    private List<Adressetilknytning> opprettAdressehistorikk(LocalDate fodtDato) {
        if (adresser.isEmpty()) {
            List<AdressetilknytningBuilder> adresser = new ArrayList<>();
            adresser.add(adresse().tilknytning(BOSTEDSADRESSE));
            LocalDate utflyttingsdato = random().dateBetween(fodtDato.plusYears(18), fodtDato.plusYears(23));
            if (!utflyttingsdato.isAfter(LocalDate.now())) {
                adresser.add(adresse()
                        .tilknytning(BOSTEDSADRESSE)
                        .fra(utflyttingsdato));
            }
            return AdressetilknytningBuilder.lagAdressehistorikk(adresser, fodtDato);
        }
        return AdressetilknytningBuilder.lagAdressehistorikk(adresser, fodtDato);
    }

    public List<TestPerson> opprett(int antall) {
        ArrayList<TestPerson> personer = new ArrayList<>(antall);
        for (int i = 0; i < antall; i++) {
            personer.add(opprett());
        }
        return personer;
    }

    private Foreldre finnForeldre(TestPerson tmpPerson) {
        return foreldreBuilder != null
                ? foreldreBuilder.get(tmpPerson)
                : new Foreldre(null, null);
    }

    private LocalDate beregnFodtDato() {
        if (fodtDato != null) {
            return fodtDato;
        }
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return random().dateBetween(
                tomorrow.minusYears(alderMax + 1),
                tomorrow.minusYears(alderMin));
    }

    @Data
    public static class RelasjonBuilder {
        private final TestPersonBuilder personBuilder;
        private final Relasjon[] relasjoner;
    }
}
