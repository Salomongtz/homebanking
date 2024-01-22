package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils {

    private CardUtils() {
    }

    public static String getCardNumber() {
        return "4%s %s %s %s".formatted(
                String.format("%03d", new Random().nextInt(0, 1000)),
                String.format("%04d", new Random().nextInt(0, 10000)),
                String.format("%04d", new Random().nextInt(0, 10000)),
                String.format("%04d", new Random().nextInt(0, 10000))
        );
    }

    public static String getCardCVV() {
        return String.format("%03d", new Random().nextInt(0, 1000));
    }
}
