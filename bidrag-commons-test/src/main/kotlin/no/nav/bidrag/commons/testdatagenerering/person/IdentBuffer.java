package no.nav.bidrag.commons.testdatagenerering.person;


import no.nav.bidrag.commons.testdatagenerering.RandomTestData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.StringUtils.repeat;

public class IdentBuffer {
    private static final int[] K1_VEKT = { 3, 7, 6, 1, 8, 9, 4, 5, 2 };
    private static final int[] K2_VEKT = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
    private StringBuffer buffer = new StringBuffer(11);

    public IdentBuffer withDate(LocalDate date) {
        buffer.replace(0, 6, DateTimeFormatter.ofPattern("ddMMyy").format(date));
        return this;
    }

    public IdentBuffer increaseDigit(int pos, int increaseBy) {
        buffer.replace(pos, pos + 1, Integer.toString(Integer.parseInt(buffer.substring(pos, pos + 1)) + increaseBy));
        return this;
    }

    public IdentBuffer withPersonnummer(Kjonn kjonn, int fodtAar) {
        try {
            set(6, 3, randomPersonnummer(kjonn, fodtAar));
            return beregnKontrollsiffer();

        } catch (IllegalStateException e) {
            return withPersonnummer(kjonn, fodtAar);
        }
    }

    public IdentBuffer withDodfodtNr(int dodfodtNr) {
        set(6, 5, dodfodtNr);
        return this;
    }

    private int randomPersonnummer(Kjonn kjonn, int fodtAar) {
        if (1940 <= fodtAar && fodtAar <= 1999) {
            return randomPersonnummer(kjonn, 900, 999);

        } else if (1854 <= fodtAar && fodtAar <= 1899) {
            return randomPersonnummer(kjonn, 500, 749);

        } else if (1900 <= fodtAar && fodtAar <= 1999) {
            return randomPersonnummer(kjonn, 0, 499);

        } else if (2000 <= fodtAar && fodtAar <= 2039) {
            return randomPersonnummer(kjonn, 500, 999);
        }
        throw new IllegalArgumentException("Fant ikke gyldig serie for årstallet " + fodtAar);
    }

    private int randomPersonnummer(Kjonn kjonn, int fraInklusiv, int tilInklusiv) {
        int antall = (tilInklusiv - fraInklusiv + 1) / 2;
        return (RandomTestData.random().getRandom().nextInt(antall) * 2)
                + fraInklusiv
                + (Kjonn.MANN.equals(kjonn) ? 1 : 0);
    }

    public IdentBuffer beregnKontrollsiffer() {
        set(9, 1, kontrollsiffer(K1_VEKT));
        set(10, 1, kontrollsiffer(K2_VEKT));
        return this;
    }

    public IdentBuffer set(int pos, int digits, int value) {
        buffer.replace(pos, pos + digits, leftPad(digits, Integer.toString(value)));
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
