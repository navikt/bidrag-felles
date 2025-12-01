package no.nav.bidrag.commons.testdatagenerering.time;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.time.LocalDate;

import static no.nav.bidrag.commons.testdatagenerering.time.DateBuilder.forsteDag;
import static no.nav.bidrag.commons.testdatagenerering.time.DateBuilder.sisteDag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DateBuilderTest {

    @Test
    public void testIDag() {
        new Tester(new DateBuilder(null, null, null))
                .assertForDate(LocalDate.of(2022, 9, 24), is(equalTo(LocalDate.of(2022, 9, 24))));
    }

    @Test
    public void testForsteDag() {
        new Tester(forsteDag().iDenneMaaned().detteAaret())
                .assertForDate("2022-01-15", "2022-01-01")
                .assertForDate("2022-09-01", "2022-09-01")
                .assertForDate("2022-09-24", "2022-09-01")
                .assertForDate("2022-09-30", "2022-09-01");
    }

    @Test
    public void testSisteDag() {
        new Tester(sisteDag().iDenneMaaned().detteAaret())
                .assertForDate("2022-01-15", "2022-01-31")
                .assertForDate("2022-09-01", "2022-09-30")
                .assertForDate("2022-09-24", "2022-09-30")
                .assertForDate("2022-09-30", "2022-09-30");
    }

    @Test
    public void testSisteDagForrigeMaaned() {
        new Tester(sisteDag().iForrigeMaaned().detteAaret())
                .assertForDate("2020-03-31", "2020-02-29")
                .assertForDate("2022-01-15", "2021-12-31")
                .assertForDate("2022-03-31", "2022-02-28")
                .assertForDate("2022-09-01", "2022-08-31")
                .assertForDate("2022-09-24", "2022-08-31")
                .assertForDate("2022-09-30", "2022-08-31");
    }

    @Test

    public void testForsteDagNesteMaaned() {
        new Tester(forsteDag().iNesteMaaned().detteAaret())
                .assertForDate("2020-03-31", "2020-04-01")
                .assertForDate("2022-01-15", "2022-02-01")
                .assertForDate("2022-03-31", "2022-04-01")
                .assertForDate("2022-09-01", "2022-10-01")
                .assertForDate("2022-09-24", "2022-10-01")
                .assertForDate("2022-09-30", "2022-10-01");
    }

    @RequiredArgsConstructor
    public static class Tester {
        private final DateBuilder dateBuilder;

        public LocalDate getForDate(LocalDate relativeTo) {
            return new DateBuilder(d -> relativeTo, dateBuilder, null).get();
        }

        public Tester assertForDate(LocalDate relativeTo, Matcher<LocalDate> matcher) {
            assertThat(getForDate(relativeTo), matcher);
            return this;
        }

        public Tester assertForDate(String relativeTo, String expected) {
            assertThat(getForDate(LocalDate.parse(relativeTo)).toString(), is(equalTo(expected)));
            return this;
        }
    }
}