package no.nav.bidrag.commons.testdatagenerering.konto;

import static no.nav.bidrag.commons.testdatagenerering.RandomTestData.random;
import static org.apache.commons.lang3.StringUtils.repeat;

public class NorskKontonummerBuilder {
    private static final int[] VEKT = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2}; // 5432765432
    private StringBuffer buffer = new StringBuffer(11);

    public NorskKontonummerBuilder medBankNummer(int bankNummer) {
        return set(0, 4, bankNummer);
    }

    public NorskKontonummerBuilder medKontotype(int kontotype) {
        return set(4, 2, kontotype);
    }

    public NorskKontonummerBuilder set(int pos, int digits, int value) {
        buffer.replace(pos, pos + digits, leftPad(digits, Integer.toString(value)));
        return this;
    }

    public NorskKontonummerBuilder medKundenummer(int kundenummer) {
        set(6, 4, kundenummer);
        return beregnKontrollsiffer();
    }

    public NorskKontonummerBuilder randomKundenummer() {
        try {
            set(6, 4, random().integerUpTo(10000));
            return beregnKontrollsiffer();

        } catch (IllegalStateException e) {
            return randomKundenummer();
        }
    }

    public NorskKontonummerBuilder beregnKontrollsiffer() {
        set(10, 1, kontrollsiffer(VEKT));
        return this;
    }

    public String leftPad(int length, String str) {
        String padded = repeat("0", Math.max(0, length - str.length())) + str;
        return padded.substring(padded.length() - length);
    }

    private int kontrollsiffer(int[] vekting) {
        int sum = 0;
        for (int i = 0; i < vekting.length; i++) {
            sum += Integer.parseInt(buffer.substring(i, i + 1)) * vekting[i];
        }
        int kontrollsiffer = 11 - (sum % 11);
        if (kontrollsiffer == 11) {
            kontrollsiffer = 0;
        }
        if (kontrollsiffer > 9) {
            throw new IllegalStateException();
        }
        return kontrollsiffer;
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
}
