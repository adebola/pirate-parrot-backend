package io.factorialsystems.msscpirateparrotorder.model;

public enum OrderShipmentStatus {
    PENDING, EN_ROUTE, DELIVERED;

    public static OrderShipmentStatus fromInteger(int x) {
        return switch (x) {
            case 0 -> PENDING;
            case 1 -> EN_ROUTE;
            case 2 -> DELIVERED;
            default -> null;
        };
    }
}
