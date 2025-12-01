package no.nav.bidrag.commons.testdatagenerering.person;


import no.nav.bidrag.commons.testdatagenerering.RandomTestData;

import java.time.LocalDate;
import java.util.function.Consumer;

import static no.nav.bidrag.commons.testdatagenerering.person.Kjonn.KVINNE;
import static no.nav.bidrag.commons.testdatagenerering.person.Kjonn.MANN;
import static no.nav.bidrag.commons.testdatagenerering.person.TestPersonBuilder.person;


public class ForeldreBuilder {
    private static final Consumer<TestPersonBuilder> NOOP_VISITOR = (p) -> {
    };
    private Consumer<TestPersonBuilder> morVisitor;
    private Relasjon[] morRelasjoner;
    private Consumer<TestPersonBuilder> farVisitor;
    private Relasjon[] farRelasjoner;
    private Relasjon[] relasjonerTilHverandre;

    public static ForeldreBuilder foreldre() {
        return new ForeldreBuilder();
    }

    public ForeldreBuilder mor(Consumer<TestPersonBuilder> morVisitor, Relasjon... relasjoner) {
        this.morVisitor = morVisitor;
        this.morRelasjoner = relasjoner;
        return this;
    }

    public ForeldreBuilder mor(Relasjon... relasjoner) {
        return mor(NOOP_VISITOR, relasjoner);
    }

    public ForeldreBuilder far(Consumer<TestPersonBuilder> farVisitor, Relasjon... relasjoner) {
        this.farVisitor = farVisitor;
        this.farRelasjoner = relasjoner;
        return this;
    }

    public ForeldreBuilder far(Relasjon... relasjoner) {
        return far(NOOP_VISITOR, relasjoner);
    }

    public ForeldreBuilder relasjonTilHverandre(Relasjon... relasjoner) {
        this.relasjonerTilHverandre = relasjoner;
        return this;
    }

    Foreldre get(TestPerson tmpPerson) {
        TestPerson mor = getForelder(KVINNE, morVisitor, tmpPerson, morRelasjoner, null, null);
        TestPerson far = getForelder(MANN, farVisitor, tmpPerson, farRelasjoner, mor, relasjonerTilHverandre);
        return new Foreldre(mor, far);
    }

    TestPerson getForelder(
            Kjonn kjonn,
            Consumer<TestPersonBuilder> visitor,
            TestPerson tmpPerson,
            Relasjon[] relasjoner,
            TestPerson motForelder,
            Relasjon[] foreldrerelasjoner) {

        if (visitor != null) {
            LocalDate barnFodt = tmpPerson.getFodselsdato();

            TestPersonBuilder personBuilder = person()
                    .kjonn(kjonn)
                    .fodtDato(RandomTestData.random().dateBetween(barnFodt.minusYears(30), barnFodt.minusYears(20)));

            if (motForelder != null) {
                Relasjon.relater(motForelder, personBuilder, foreldrerelasjoner);
            }
            Relasjon.relater(tmpPerson, personBuilder, relasjoner);

            visitor.accept(personBuilder);

            return personBuilder.opprett();
        }
        return null;
    }
}