package sk.uniba.fmph.dcs.power_grid;

import java.util.Objects;

/**
 * An explanation of why a game request was rejected.
 */
public final class RequestError {

    private final String description;

    /**
     * Creates an explanation for a rejected request.
     *
     * @param description nonblank explanation
     * @throws NullPointerException if description is null
     * @throws IllegalArgumentException if description is blank
     */
    public RequestError(final String description) {
        Objects.requireNonNull(description, "description must not be null");
        if (description.isBlank()) {
            throw new IllegalArgumentException("description must not be blank");
        }
        this.description = description;
    }

    /**
     * Returns the explanation for the rejected request.
     *
     * @return the nonblank description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RequestError otherError)) {
            return false;
        }
        return description.equals(otherError.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    @Override
    public String toString() {
        return description;
    }
}
