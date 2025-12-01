package no.nav.bidrag.commons.testdatagenerering;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomTestData {
    private static final Object lock = new Object();
    private static RandomTestData instance;
    private final Random random;

    public static RandomTestData random() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new RandomTestData(defaultRandom());
                }
            }
        }
        return instance;
    }

    private static Random defaultRandom() {
        try {
            String seedProperty = System.getProperty("testdata.randomseed");
            if (seedProperty != null) {
                return new Random(Long.parseLong(seedProperty));
            }
        } catch (Exception e) {
            //
        }
        return new Random();
    }

    public RandomTestData(Random random) {
        this.random = random;
    }

    public Random getRandom() {
        return random;
    }

    @SafeVarargs
    public final <T> T oneOf(T... values) {
        return values[random.nextInt(values.length)];
    }

    public final <E extends Enum<E>> E oneOf(Class<E> enumClass) {
        return oneOf(enumClass.getEnumConstants());
    }

    public final LocalDate dateBetween(LocalDate fromInclusive, LocalDate toExclusive) {
        long numberOfDays = ChronoUnit.DAYS.between(fromInclusive, toExclusive);
        return fromInclusive.plusDays(nextLong(numberOfDays));
    }

    public int integerUnder(int bound) {
        return random.nextInt(bound);
    }

    public int integerUpTo(int toInclusive) {
        return integerUnder(toInclusive + 1);
    }

    public long nextLong(long bound) {
        return Math.abs(random.nextLong()) % bound;
    }

    public static <T> List<T> shuffle(List<T> list) {
        List<T> shuffledList = new ArrayList<>(list);
        Collections.shuffle(shuffledList, random().getRandom());
        return shuffledList;
    }
}
