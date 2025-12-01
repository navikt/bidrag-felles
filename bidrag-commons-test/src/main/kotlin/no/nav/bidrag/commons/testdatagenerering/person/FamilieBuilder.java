package no.nav.bidrag.commons.testdatagenerering.person;

import lombok.RequiredArgsConstructor;
import no.nav.bidrag.commons.testdatagenerering.RandomTestData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static no.nav.bidrag.commons.testdatagenerering.person.Kjonn.KVINNE;
import static no.nav.bidrag.commons.testdatagenerering.person.Kjonn.MANN;
import static no.nav.bidrag.commons.testdatagenerering.person.TestPersonBuilder.person;


public class FamilieBuilder {
    private static final Consumer<TestPersonBuilder> NOOP_VISITOR = (p) -> {
    };

    private Consumer<TestPersonBuilder> partnerVisitor;
    private Relasjon[] partnerRelasjoner;
    private List<BarnBuilder> barnListe = new ArrayList<>();

    public static FamilieBuilder familie() {
        return new FamilieBuilder();
    }

    public FamilieBuilder partner(Consumer<TestPersonBuilder> partnerVisitor, Relasjon... relasjoner) {
        this.partnerVisitor = partnerVisitor;
        this.partnerRelasjoner = relasjoner;
        return this;
    }

    public FamilieBuilder partner(Relasjon... relasjoner) {
        return partner(NOOP_VISITOR, relasjoner);
    }

    public FamilieBuilder barn(Consumer<TestPersonBuilder> barnVisitor, Relasjon... relasjoner) {
        if (relasjoner != null) {
            relasjoner = new Relasjon[] { Relasjon.SAMME_ETTERNAVN, Relasjon.SAMME_ADRESSE };
        }
        this.barnListe.add(new BarnBuilder(barnVisitor, relasjoner, null));
        return this;
    }

    public FamilieBuilder barn(Relasjon... relasjoner) {
        return barn(NOOP_VISITOR, relasjoner);
    }

    public FamilieBuilder barnMedRelasjonTilPartner(Consumer<TestPersonBuilder> barnVisitor, Relasjon... relasjoner) {
        if (relasjoner != null) {
            relasjoner = new Relasjon[] { Relasjon.SAMME_ETTERNAVN, Relasjon.SAMME_ADRESSE };
        }
        this.barnListe.add(new BarnBuilder(barnVisitor, null, relasjoner));
        return this;
    }

    public FamilieBuilder barnMedRelasjonTilPartner(Relasjon... relasjoner) {
        return barnMedRelasjonTilPartner(NOOP_VISITOR, relasjoner);
    }

    public void get(TestPerson person) {
        TestPerson partner = getPartner(person);

        for (BarnBuilder barn : barnListe) {
            barn.get(person, partner);
        }
    }

    private TestPerson getPartner(TestPerson person) {
        if (partnerVisitor != null) {
            TestPersonBuilder builder = person()
                    .fodtDato(RandomTestData.random()
                            .dateBetween(person.getFodselsdato().minusYears(2), person.getFodselsdato().plusYears(2)))
                    .kjonn(MANN.equals(person.getKjonn()) ? KVINNE : MANN);

            Relasjon.relater(person, builder, partnerRelasjoner);
            partnerVisitor.accept(builder);
            return builder.opprett();
        }
        return null;
    }

    @RequiredArgsConstructor
    private static class BarnBuilder {
        private final Consumer<TestPersonBuilder> visitor;
        private final Relasjon[] relasjoner;
        private final Relasjon[] relasjonerTilPartner;

        public void get(TestPerson person, TestPerson partner) {
            LocalDate yngsteForeldreFodtDato = null;
            yngsteForeldreFodtDato = person.getFodselsdato();
            if (partner != null
                    && partner.getFodselsdato().isAfter(yngsteForeldreFodtDato)) {
                yngsteForeldreFodtDato = partner.getFodselsdato();
            }
            TestPerson mor = KVINNE.equals(person) ? person : partner;
            TestPerson far = KVINNE.equals(person) ? partner : person;

            TestPersonBuilder builder = person()
                    .fodtDato(RandomTestData.random().dateBetween(yngsteForeldreFodtDato.plusYears(18), LocalDate.now()))
                    .mor(mor)
                    .far(far);

            Relasjon.relater(person, builder, relasjoner);
            if (partner != null) {
                Relasjon.relater(partner, builder, relasjonerTilPartner);
            }
            visitor.accept(builder);
            builder.opprett();
        }
    }
}
