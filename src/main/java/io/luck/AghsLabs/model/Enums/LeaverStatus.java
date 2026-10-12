package io.luck.AghsLabs.model.Enums;

public enum LeaverStatus {

    DID_NOT_LEAVE(0, "Didn't leave"),
    LEFT_SAFELY(1, "Left safely"),
    ABANDONED(2, "Abandoned"),
    UNKNOWN(-1, "No data");

    private final int value;

    LeaverStatus(int value, String description) {
        this.value = value;
    }

    public static LeaverStatus fromValue(int value) {
        for (LeaverStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }

        return UNKNOWN;
    }

}