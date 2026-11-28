package sk.uniba.fmph.dcs.power_grid;

import java.util.Objects;

/**
 * Represents either a successful value or a rejected game request.
 * @param <T> the type of a successful result
 */
public final class Result<T> {

    private final T value;
    private final RequestError error;

    private Result(final T value, final RequestError error) {
        this.value = value;
        this.error = error;
    }

    /**
     * Creates a successful result.
     *
     * @param value the successful value
     * @param <T> the value type
     * @return a successful result containing the value
     * @throws NullPointerException if value is null
     */
    public static <T> Result<T> success(final T value) {
        return new Result<>(Objects.requireNonNull(value), null);
    }

    /**
     * Creates a failed result.
     *
     * @param error the reason the request was rejected
     * @param <T> the value type that would have been returned on success
     * @return a failed result containing the error
     * @throws NullPointerException if error is null
     */
    public static <T> Result<T> failure(final RequestError error) {
        return new Result<>(null, Objects.requireNonNull(error));
    }

    /**
     * Checks whether this result contains a successful value.
     *
     * @return true for success, false for failure
     */
    public boolean isSuccess() {
        return error == null;
    }

    /**
     * Checks whether this result contains an error.
     *
     * @return true for failure, false for success
     */
    public boolean isFailure() {
        return error != null;
    }

    /**
     * Returns the successful value.
     *
     * @return the value
     * @throws IllegalStateException if this result is a failure
     */
    public T getValue() {
        if (isFailure()) {
            throw new IllegalStateException("A failed result has no success value.");
        }
        return value;
    }

    /**
     * Returns the error explaining the rejected request.
     *
     * @return the error
     * @throws IllegalStateException if this result is a success
     */
    public RequestError getError() {
        if (isSuccess()) {
            throw new IllegalStateException("A successful result has no error.");
        }
        return error;
    }
}
