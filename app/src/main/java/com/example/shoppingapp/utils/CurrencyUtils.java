package com.example.shoppingapp.utils;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyUtils {

    private CurrencyUtils() {
    }

    public static String formatCurrency(double amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return numberFormat.format(amount);
    }
}
