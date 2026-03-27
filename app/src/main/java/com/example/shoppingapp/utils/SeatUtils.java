package com.example.shoppingapp.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class SeatUtils {

    private static final int SEATS_PER_ROW = 10;

    private SeatUtils() {
    }

    @NonNull
    public static String normalizeSeatNumber(String seatNumber) {
        if (seatNumber == null) {
            return "";
        }
        return seatNumber.trim().replace(" ", "").toUpperCase(Locale.ROOT);
    }

    @NonNull
    public static List<String> generateSeatNumbers(int totalSeats) {
        if (totalSeats <= 0) {
            return Collections.emptyList();
        }

        List<String> seatNumbers = new ArrayList<>(totalSeats);
        for (int index = 0; index < totalSeats; index++) {
            int rowIndex = index / SEATS_PER_ROW;
            int seatIndex = (index % SEATS_PER_ROW) + 1;
            seatNumbers.add(toRowLabel(rowIndex) + seatIndex);
        }
        return seatNumbers;
    }

    public static boolean isSeatNumberValid(String seatNumber, int totalSeats) {
        String normalizedSeat = normalizeSeatNumber(seatNumber);
        if (normalizedSeat.isEmpty() || totalSeats <= 0) {
            return false;
        }

        return generateSeatNumbers(totalSeats).contains(normalizedSeat);
    }

    @NonNull
    public static List<String> getAvailableSeatNumbers(int totalSeats, List<String> bookedSeatNumbers) {
        List<String> allSeatNumbers = generateSeatNumbers(totalSeats);
        if (allSeatNumbers.isEmpty()) {
            return allSeatNumbers;
        }

        Set<String> bookedSeats = new HashSet<>();
        if (bookedSeatNumbers != null) {
            for (String seatNumber : bookedSeatNumbers) {
                String normalizedSeat = normalizeSeatNumber(seatNumber);
                if (!normalizedSeat.isEmpty()) {
                    bookedSeats.add(normalizedSeat);
                }
            }
        }

        List<String> availableSeats = new ArrayList<>(allSeatNumbers.size());
        for (String seatNumber : allSeatNumbers) {
            if (!bookedSeats.contains(seatNumber)) {
                availableSeats.add(seatNumber);
            }
        }
        return availableSeats;
    }

    @NonNull
    private static String toRowLabel(int rowIndex) {
        StringBuilder builder = new StringBuilder();
        int currentIndex = rowIndex;

        // Uses spreadsheet-like row labels: A..Z, AA..AZ, ...
        do {
            builder.insert(0, (char) ('A' + (currentIndex % 26)));
            currentIndex = (currentIndex / 26) - 1;
        } while (currentIndex >= 0);

        return builder.toString();
    }
}
