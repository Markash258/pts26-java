package sk.uniba.fmph.dcs.power_grid;

/**
 * An immutable value identifying a power plant by its card number.
 */
public final class PowerPlantNumber implements Comparable<PowerPlantNumber> {

    private final int number;

    /**
     * Creates a power plant number.
     *
     * @param number the positive card number
     * @throws IllegalArgumentException if the number is zero or negative
     */
    public PowerPlantNumber(final int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Power plant number must be positive.");
        }
        this.number = number;
    }

    /**
     * Returns the card number.
     *
     * @return the positive power plant number
     */
    public int getNumber() {
        return number;
    }

    @Override
    public int compareTo(final PowerPlantNumber other) {
        return Integer.compare(number, other.number);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PowerPlantNumber otherNumber)) {
            return false;
        }
        return number == otherNumber.number;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(number);
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }
}
