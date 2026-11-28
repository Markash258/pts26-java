package sk.uniba.fmph.dcs.power_grid;

import java.util.List;
import java.util.Objects;

/**
 * An immutable description of a power plant card.
 */
public final class PowerPlant {

    private final PowerPlantNumber number;
    private final List<Resource> acceptedResources;
    private final int resourceAmount;
    private final int numberOfCities;

    /**
     * Creates a power plant card.
     *
     * @param number the card number
     * @param acceptedResources the types of fuel this plant can use
     * @param resourceAmount the total units of fuel needed to run the plant
     * @param numberOfCities the number of cities the plant can power
     * @throws NullPointerException if the number, fuel list, or a fuel type is null
     * @throws IllegalArgumentException if either quantity is negative
     */
    public PowerPlant(final PowerPlantNumber number, final List<Resource> acceptedResources,
            final int resourceAmount, final int numberOfCities) {
        this.number = Objects.requireNonNull(number, "number must not be null");
        this.acceptedResources = List.copyOf(acceptedResources);
        if (resourceAmount < 0) {
            throw new IllegalArgumentException("resourceAmount must not be negative");
        }
        if (numberOfCities < 0) {
            throw new IllegalArgumentException("numberOfCities must not be negative");
        }
        this.resourceAmount = resourceAmount;
        this.numberOfCities = numberOfCities;
    }

    /**
     * Returns the card number.
     *
     * @return the card number
     */
    public PowerPlantNumber getNumber() {
        return number;
    }

    /**
     * Returns the types of fuel the plant accepts.
     *
     * @return an unmodifiable list of accepted resources
     */
    public List<Resource> getAcceptedResources() {
        return acceptedResources;
    }

    /**
     * Returns how much fuel the plant needs to run.
     *
     * @return the total units of fuel, or zero for a plant that needs no fuel
     */
    public int getResourceAmount() {
        return resourceAmount;
    }

    /**
     * Returns the plant's capacity.
     *
     * @return the number of cities the plant can power
     */
    public int getNumberOfCities() {
        return numberOfCities;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PowerPlant plant)) {
            return false;
        }
        return number.equals(plant.number)
                && acceptedResources.equals(plant.acceptedResources)
                && resourceAmount == plant.resourceAmount
                && numberOfCities == plant.numberOfCities;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, acceptedResources, resourceAmount, numberOfCities);
    }

    @Override
    public String toString() {
        return "PowerPlant[number=" + number
                + ", acceptedResources=" + acceptedResources
                + ", resourceAmount=" + resourceAmount
                + ", numberOfCities=" + numberOfCities + "]";
    }
}
