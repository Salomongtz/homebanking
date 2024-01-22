package com.mindhub.homebanking.utils;

import java.util.Random;

public final class AccountUtils {
    public static String getAccountNumber() {
        return "VIN-%s".formatted(String.format("%06d", new Random().nextInt(0, 1000000)));
    }
}
