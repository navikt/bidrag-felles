package no.nav.bidrag.commons.testdatagenerering.time;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static no.nav.bidrag.commons.testdatagenerering.RandomTestData.random;

public class DateBuilder {
    private final List<Function<? super LocalDate, ? extends LocalDate>> modifiers = new ArrayList<>();

    DateBuilder(
            Function<? super LocalDate, ? extends LocalDate> prepend,
            DateBuilder parent,
            Function<? super LocalDate, ? extends LocalDate> append) {

        if (prepend != null) {
            modifiers.add(prepend);
        }
        if (parent != null) {
            modifiers.addAll(parent.modifiers);
        }
        if (append != null) {
            modifiers.add(append);
        }
    }

    public static MaanedBuilder forsteDag() {
        return new MaanedBuilder(new DateBuilder(d -> d.withDayOfMonth(1), null, null));
    }

    public static MaanedBuilder denneDag() {
        return new MaanedBuilder(new DateBuilder(null, null, null));
    }

    public static MaanedBuilder sisteDag() {
        return new MaanedBuilder(new DateBuilder(d -> d.withDayOfMonth(d.lengthOfMonth()), null, null));
    }

    public static MaanedBuilder tilfeldigDag() {
        return new MaanedBuilder(new DateBuilder(d -> d.withDayOfMonth(random().integerUpTo(d.lengthOfMonth())), null, null));
    }

    public static MaanedBuilder dag(int dagNr) {
        return new MaanedBuilder(new DateBuilder(d -> d.withDayOfMonth(dagNr), null, null));
    }

    public static AarBuilder iMorgen() {
        return new AarBuilder(new DateBuilder(d -> d.plusDays(1), null, null));
    }

    @RequiredArgsConstructor
    public static class MaanedBuilder {
        private final DateBuilder dateBuilder;

        public AarBuilder iForrigeMaaned() {
            return new AarBuilder(new DateBuilder(d -> d.minusMonths(1), dateBuilder, null));
        }

        public AarBuilder iDenneMaaned() {
            return new AarBuilder(dateBuilder);
        }

        public AarBuilder iNesteMaaned() {
            return new AarBuilder(new DateBuilder(d -> d.plusMonths(1), dateBuilder, null));
        }

        public AarBuilder omNMaaneder(int antallMaaneder) {
            return new AarBuilder(new DateBuilder(d -> d.plusMonths(antallMaaneder), dateBuilder, null));
        }
    }

    @RequiredArgsConstructor
    public static class AarBuilder {
        private final DateBuilder dateBuilder;

        public DateBuilder nAarSiden(int antallAarSiden) {
            return new DateBuilder(d -> d.minusYears(antallAarSiden), dateBuilder, null);
        }

        public DateBuilder detteAaret() {
            return dateBuilder;
        }

        public DateBuilder omNAar(int antallAar) {
            return new DateBuilder(d -> d.plusYears(antallAar), dateBuilder, null);
        }
    }

    public LocalDate get() {
        LocalDate date = LocalDate.now();
        for (Function<? super LocalDate, ? extends LocalDate> modifier : modifiers) {
            date = modifier.apply(date);
        }
        return date;
    }
}