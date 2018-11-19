package com.hordiienko.slotmachine.utils;

import java.util.Random;

public class RandomGenerator {
    public static int getIntChance(int from, int toInclusive, int expected, int chance) {
        int randomInt = getRandomIntInclusive(1, 100);

        if (randomInt <= chance) {
            return expected;
        } else {
            return getRandomIntInclusive(from, toInclusive);
        }
    }

    public static int getRandomIntInclusive(int from, int toInclusive) {
        return new Random().nextInt(toInclusive - from + 1) + from;
    }
}
