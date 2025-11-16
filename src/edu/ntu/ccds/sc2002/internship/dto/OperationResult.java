package edu.ntu.ccds.sc2002.internship.dto;

/**
 * Data Transfer Object representing the result of an operation.
 * DTO LAYER: Transfers operation outcome data (success/failure with message).
 */
public class OperationResult {
    private final boolean success;
    private final String message;

    public OperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    // Factory methods for convenience
    public static OperationResult success(String message) {
        return new OperationResult(true, message);
    }

    public static OperationResult failure(String message) {
        return new OperationResult(false, message);
    }
}
