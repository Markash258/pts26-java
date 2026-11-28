package sk.uniba.fmph.dcs.power_grid;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Calculates connection fees within a fixed playing zone.
 */
public final class Connections {

    private static final long UNREACHABLE = Long.MAX_VALUE;

    /**
     * An undirected link between two cities in the playing zone.
     *
     * @param from one endpoint
     * @param to the other endpoint
     * @param cost the nonnegative connection fee in Elektro
     */
    public record Link(String from, String to, int cost) {
        /**
         * Creates a link with the given endpoints and fee.
         *
         * @throws NullPointerException if either endpoint is null
         * @throws IllegalArgumentException if either endpoint is blank or the fee is negative
         */
        public Link {
            Objects.requireNonNull(from, "from");
            Objects.requireNonNull(to, "to");
            if (from.isBlank() || to.isBlank()) {
                throw new IllegalArgumentException("City names must not be blank.");
            }
            if (cost < 0) {
                throw new IllegalArgumentException("Connection costs must not be negative.");
            }
        }
    }

    private final Map<String, Integer> cityIndexes;
    private final long[][] cheapestCosts;

    /**
     * Builds a connection calculator from the cities and links in the selected playing zone.
     * Include cities with no links in {@code cities} so they can be selected as starting cities.
     *
     * @param cities the case-sensitive city names in the zone
     * @param links the links within the zone
     * @throws IllegalArgumentException if a city name is blank or a link endpoint is outside the zone
     * @throws NullPointerException if an argument, city name, or link is null
     */
    public Connections(final Collection<String> cities, final List<Link> links) {
        Objects.requireNonNull(cities, "cities");
        Objects.requireNonNull(links, "links");
        cityIndexes = indexCities(cities);
        cheapestCosts = buildDirectCosts(links);
        calculateCheapestRoutes();
    }

    /**
     * Calculates the connection fees for adding cities in the supplied build order.
     * Each target can use any previously connected city, including earlier targets, as the
     * start of its route. Routes may pass through other cities without adding them to the network.
     * The first city in an empty network has a connection fee of zero.
     *
     * @param from cities already in the player's network
     * @param to new cities in the player's chosen build order
     * @return the total connection fee in Elektro, or zero for an empty build list
     * @throws IllegalArgumentException if a city is unknown, a target is already in the network
     *         or repeated in the build list, or a target is unreachable
     * @throws NullPointerException if either list or a city name is null
     * @throws ArithmeticException if the total connection fee exceeds the range of an int
     */
    public int calculateConnectingCost(final List<String> from, final List<String> to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Set<Integer> network = new HashSet<>();
        for (String city : from) {
            network.add(cityIndex(city));
        }

        long total = 0;
        for (String city : to) {
            int target = cityIndex(city);
            if (network.contains(target)) {
                throw new IllegalArgumentException("City is already in the network: " + city);
            }
            total += connectionFee(network, target, city);
            network.add(target);
        }
        return Math.toIntExact(total);
    }

    private long connectionFee(final Set<Integer> network, final int target, final String city) {
        if (network.isEmpty()) {
            return 0;
        }
        long fee = UNREACHABLE;
        for (int source : network) {
            fee = Math.min(fee, cheapestCosts[source][target]);
        }
        if (fee == UNREACHABLE) {
            throw new IllegalArgumentException("City cannot be reached from the network: " + city);
        }
        return fee;
    }

    private static Map<String, Integer> indexCities(final Collection<String> cities) {
        Map<String, Integer> indexes = new HashMap<>();
        for (String city : cities) {
            Objects.requireNonNull(city, "city name");
            if (city.isBlank()) {
                throw new IllegalArgumentException("City names must not be blank.");
            }
            indexes.putIfAbsent(city, indexes.size());
        }
        return Map.copyOf(indexes);
    }

    private int cityIndex(final String city) {
        Objects.requireNonNull(city, "city name");
        Integer index = cityIndexes.get(city);
        if (index == null) {
            throw new IllegalArgumentException("Unknown city: " + city);
        }
        return index;
    }

    private long[][] buildDirectCosts(final List<Link> links) {
        long[][] costs = new long[cityIndexes.size()][cityIndexes.size()];
        for (int city = 0; city < costs.length; city++) {
            Arrays.fill(costs[city], UNREACHABLE);
            costs[city][city] = 0;
        }
        for (Link link : links) {
            Objects.requireNonNull(link, "link");
            int from = cityIndex(link.from());
            int to = cityIndex(link.to());
            long cost = Math.min(costs[from][to], link.cost());
            costs[from][to] = cost;
            costs[to][from] = cost;
        }
        return costs;
    }

    // Uses Floyd–Warshall algorithm
    private void calculateCheapestRoutes() {
        // For each possible intermediate city, check whether going through it makes the route cheaper.
        for (int via = 0; via < cheapestCosts.length; via++) {
            for (int from = 0; from < cheapestCosts.length; from++) {
                long toIntermediate = cheapestCosts[from][via];
                if (toIntermediate == UNREACHABLE) {
                    continue;
                }
                for (int to = 0; to < cheapestCosts.length; to++) {
                    long fromIntermediate = cheapestCosts[via][to];
                    if (fromIntermediate != UNREACHABLE) {
                        long costCandidate = toIntermediate + fromIntermediate;
                        cheapestCosts[from][to] = Math.min(cheapestCosts[from][to], costCandidate);
                    }
                }
            }
        }
    }
}
