package no.nav.bidrag.commons.testdatagenerering.adresse;

import no.nav.bidrag.commons.testdatagenerering.RandomTestData;

import static no.nav.bidrag.commons.testdatagenerering.adresse.Adresseformat.UTENLANDSK_ADRESSE;
import static no.nav.bidrag.commons.testdatagenerering.adresse.Adresseformat.UTENLANDSK_ADRESSE_I_FRITT_FORMAT;
import static no.nav.bidrag.commons.testdatagenerering.adresse.Adressetype.*;

public class TestAdresseBuilder {
    private Adresseformat format;
    private String adresselinje1;
    private String adresselinje2;
    private String adresselinje3;
    private String postnummer;
    private String poststed;
    private String bolignummer;
    private Land land = Land.NORGE;

    public static TestAdresseBuilder adresse() {
        return new TestAdresseBuilder();
    }

    public TestAdresseBuilder format(Adresseformat format) {
        this.format = format;
        return this;
    }

    public TestAdresseBuilder adresselinje1(String adresselinje1) {
        this.adresselinje1 = adresselinje1;
        return this;
    }

    public TestAdresseBuilder adresselinje2(String adresselinje2) {
        this.adresselinje2 = adresselinje2;
        return this;
    }

    public TestAdresseBuilder adresselinje3(String adresselinje3) {
        this.adresselinje3 = adresselinje3;
        return this;
    }

    public TestAdresseBuilder bolignummer(String bolignummer) {
        this.bolignummer = bolignummer;
        return this;
    }

    public TestAdresseBuilder postnummerOgSted(String postnummer, String poststed) {
        this.postnummer = postnummer;
        this.poststed = poststed;
        return this;
    }

    public TestAdresseBuilder land(Land land) {
        this.land = land;
        return this;
    }

    public AdressetilknytningBuilder tilknytning(Adressetype type) {
        return new AdressetilknytningBuilder(null, this, type);
    }

    public AdressetilknytningBuilder somBosted() {
        return new AdressetilknytningBuilder(null, this, BOSTEDSADRESSE);
    }

    public AdressetilknytningBuilder somOppholdsadresse() {
        return new AdressetilknytningBuilder(null, this, OPPHOLDSADRESSE);
    }

    public AdressetilknytningBuilder somKontaktadresse() {
        return new AdressetilknytningBuilder(null, this, KONTAKTADRESSE);
    }

    public TestAdresse opprett() {

        String adresselinje1 = this.adresselinje1;
        String adresselinje2 = this.adresselinje2;
        String postnummer = this.postnummer;
        String poststed = this.poststed;

        if (adresselinje1 == null) {
            adresselinje1 = isNorskAdresse()
                    ? StedListe.randomGatenavn() + " " + RandomTestData.random().nextLong(1000l) + RandomTestData.random().oneOf("", "", "A", "B", "C")
                    : RandomTestData.random().nextLong(2000) + " Weebfoot Street";
        }

        if (adresselinje2 == null && postnummer == null && poststed == null) {
            if (isNorskAdresse()) {
                Poststed postSted = StedListe.randomPostSted();
                postnummer = postSted.getPostnummer();
                poststed = postSted.getPoststed();
            } else {
                adresselinje2 = "Duckburg, Calisota";
            }
        }

        return new TestAdresse(
                format,
                adresselinje1,
                adresselinje2,
                adresselinje3,
                postnummer,
                poststed,
                bolignummer,
                land);
    }

    private boolean isNorskAdresse() {
        return Land.NORGE.equals(land)
                && !UTENLANDSK_ADRESSE.equals(format)
                && !UTENLANDSK_ADRESSE_I_FRITT_FORMAT.equals(format);
    }
}
