package io.factorialsystems.msscpirateparrotorder.model;

public enum OrderStatus {
    PENDING, PAID, COMPLETED;

    public static OrderStatus fromInteger(int x) {
        return switch (x) {
            case 0 -> PENDING;
            case 1 -> PAID;
            case 2 -> COMPLETED;
            default -> null;
        };
    }
}

