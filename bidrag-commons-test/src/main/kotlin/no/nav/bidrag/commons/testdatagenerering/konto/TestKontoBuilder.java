package no.nav.bidrag.commons.testdatagenerering.konto;

import static no.nav.bidrag.commons.testdatagenerering.RandomTestData.random;

public class TestKontoBuilder {
    private Integer bankNummer;
    private Integer kontotype;

    public static TestKontoBuilder konto() {
        return new TestKontoBuilder();
    }

    public TestKonto opprett() {
        String norskKontonummer = new NorskKontonummerBuilder()
                .medBankNummer(bankNummer != null ? bankNummer : random().integerUnder(10000))
                .medKontotype(kontotype != null ? kontotype : random().integerUnder(100))
                .randomKundenummer()
                .toString();

        return new TestKonto(norskKontonummer);
    }
}
