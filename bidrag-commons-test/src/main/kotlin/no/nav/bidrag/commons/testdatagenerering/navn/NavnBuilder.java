package no.nav.bidrag.commons.testdatagenerering.navn;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import no.nav.bidrag.commons.testdatagenerering.person.Kjonn;
import no.nav.bidrag.commons.testdatagenerering.person.NavnListe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static no.nav.bidrag.commons.testdatagenerering.time.DateUtils.parseDate;

@AllArgsConstructor
@RequiredArgsConstructor
public class NavnBuilder {
    private String fornavn;
    private String etternavn;
    private LocalDate periodeFra;
    private LocalDate periodeTil;
    private boolean tilUbestemt;

    public static NavnBuilder navn() {
        return new NavnBuilder();
    }

    public NavnBuilder(NavnBuilder base) {
        this(base.fornavn, base.etternavn, base.periodeFra, base.periodeTil, base.tilUbestemt);
    }

    public NavnBuilder fornavn(String fornavn) {
        this.fornavn = fornavn;
        return this;
    }

    public NavnBuilder etternavn(String etternavn) {
        this.etternavn = etternavn;
        return this;
    }

    public NavnBuilder fra(LocalDate periodeFra) {
        this.periodeFra = periodeFra;
        return this;
    }

    public NavnBuilder fra(String periodeFra) {
        return fra(parseDate(periodeFra));
    }

    public NavnBuilder til(LocalDate periodeTil) {
        this.periodeTil = periodeTil;
        this.tilUbestemt = false;
        return this;
    }

    public NavnBuilder til(String periodeTil) {
        return til(parseDate(periodeTil));
    }

    public NavnBuilder tilUbestemt() {
        this.periodeTil = null;
        this.tilUbestemt = true;
        return this;
    }

    public TestNavn opprett(Kjonn kjonn, LocalDate fraDato) {
        String fornavn = this.fornavn != null
                ? this.fornavn
                : NavnListe.randomFornavn(kjonn);

        String etternavn = this.etternavn != null
                ? this.etternavn
                : NavnListe.randomEtternavn();

        return new TestNavn(fornavn, etternavn, periodeFra, periodeTil);
    }

    public static List<TestNavn> lagNavnhistorikk(Collection<NavnBuilder> navnBuilderListe, Kjonn kjonn, LocalDate fraDato) {
        return periodiser(navnBuilderListe, fraDato).stream()
                .map(b -> b.opprett(kjonn, fraDato))
                .collect(toList());
    }

    private static List<NavnBuilder> periodiser(Collection<NavnBuilder> navnBuilderListe, LocalDate fraDato) {
        ArrayList<NavnBuilder> periodisert = new ArrayList<>();
        NavnBuilder forrigeBuilder = null;
        for (NavnBuilder baseNavnBuilder : navnBuilderListe) {
            NavnBuilder builder = new NavnBuilder(baseNavnBuilder);

            if (forrigeBuilder == null && builder.periodeFra == null) {
                builder.periodeFra = fraDato;
            }
            if (builder.periodeFra == null) {
                throw new IllegalArgumentException("Periode fra må være satt for adressetilknytning (unntatt første)");
            }
            if (builder.periodeFra.isBefore(fraDato)) {
                builder.periodeFra = fraDato;
            }
            if (forrigeBuilder != null && !forrigeBuilder.tilUbestemt && forrigeBuilder.periodeTil == null) {
                forrigeBuilder.periodeTil = builder.periodeFra;
            }
            periodisert.add(builder);
            forrigeBuilder = builder;
        }
        return periodisert;
    }
}
