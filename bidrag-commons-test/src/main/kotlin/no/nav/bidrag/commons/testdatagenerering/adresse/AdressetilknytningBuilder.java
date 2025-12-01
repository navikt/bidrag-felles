package no.nav.bidrag.commons.testdatagenerering.adresse;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;

import static java.util.Optional.empty;
import static no.nav.bidrag.commons.testdatagenerering.time.DateUtils.parseDate;

@RequiredArgsConstructor
@AllArgsConstructor
public class AdressetilknytningBuilder {
    private final TestAdresse adresse;
    private final TestAdresseBuilder adresseBuilder;
    private final Adressetype adressetype;
    private LocalDate periodeFra;
    private LocalDate periodeTil;
    private boolean tilUbestemt;

    public AdressetilknytningBuilder(AdressetilknytningBuilder base) {
        this(
                base.adresse,
                base.adresseBuilder,
                base.adressetype,
                base.periodeFra,
                base.periodeTil,
                base.tilUbestemt);
    }

    public AdressetilknytningBuilder fra(LocalDate periodeFra) {
        this.periodeFra = periodeFra;
        return this;
    }

    public AdressetilknytningBuilder fra(String periodeFra) {
        return fra(parseDate(periodeFra));
    }

    public AdressetilknytningBuilder til(LocalDate periodeTil) {
        this.periodeTil = periodeTil;
        this.tilUbestemt = false;
        return this;
    }

    public AdressetilknytningBuilder til(String periodeTil) {
        return til(parseDate(periodeTil));
    }

    public AdressetilknytningBuilder tilUbestemt() {
        this.periodeTil = null;
        this.tilUbestemt = true;
        return this;
    }

    public Adressetilknytning opprett() {
        return opprett(null)
                .orElseThrow(() -> new IllegalArgumentException("PeriodeTil er ikke etter PeriodeFra."));
    }

    public Optional<Adressetilknytning> opprett(LocalDate tidligsteFra) {
        TestAdresse adresse = this.adresse;
        if (adresse == null && adresseBuilder != null) {
            adresse = adresseBuilder.opprett();
        }
        if (adresse == null) {
            return empty();
        }
        LocalDate periodeFra = this.periodeFra;
        if (periodeFra == null) {
            periodeFra = tidligsteFra;
        }
        if (periodeFra == null) {
            periodeFra = LocalDate.now();
        }
        if (periodeTil != null && !periodeTil.isAfter(periodeFra)) {
            return empty();
        }

        return Optional
                .of(new Adressetilknytning(adresse, adressetype, periodeFra, periodeTil));
    }

    public static List<Adressetilknytning> lagAdressehistorikk(Iterable<AdressetilknytningBuilder> adresser, LocalDate fraDato) {
        ArrayList<Adressetilknytning> adressehistorikk = new ArrayList<>();
        for (AdressetilknytningBuilder builder : periodiser(adresser, fraDato)) {
            builder.opprett(fraDato)
                    .ifPresent(adressehistorikk::add);
        }
        return adressehistorikk;
    }

    private static List<AdressetilknytningBuilder> periodiser(Iterable<AdressetilknytningBuilder> adresser, LocalDate fraDato) {
        ArrayList<AdressetilknytningBuilder> adressehistorikk = new ArrayList<>();
        Map<Adressetype, AdressetilknytningBuilder> forrigePeriodePrType = new HashMap<>();
        for (AdressetilknytningBuilder baseBuilder : adresser) {
            AdressetilknytningBuilder builder = new AdressetilknytningBuilder(baseBuilder);
            AdressetilknytningBuilder forrigeBuilder = forrigePeriodePrType.get(builder.adressetype);
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
            adressehistorikk.add(builder);
            forrigePeriodePrType.put(builder.adressetype, builder);
        }
        return adressehistorikk;
    }
}
