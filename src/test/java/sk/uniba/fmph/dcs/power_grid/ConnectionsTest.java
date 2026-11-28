package sk.uniba.fmph.dcs.power_grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ConnectionsTest {

    private final Connections connections = new Connections(
            List.of("A", "B", "C", "D", "Isolated"),
            List.of(
                    new Connections.Link("A", "B", 4),
                    new Connections.Link("A", "B", 9),
                    new Connections.Link("B", "C", 1),
                    new Connections.Link("A", "C", 8),
                    new Connections.Link("C", "D", 0)));

    @Test
    public void usesCheaperIndirectRouteInBothDirections() {
        assertEquals(5, connections.calculateConnectingCost(List.of("A"), List.of("C")));
        assertEquals(5, connections.calculateConnectingCost(List.of("C"), List.of("A")));
    }

    @Test
    public void usesCheapestSourceFromCurrentNetwork() {
        assertEquals(1, connections.calculateConnectingCost(List.of("A", "A", "C"), List.of("B")));
    }

    @Test
    public void chargesForConnectingALaterTargetEvenIfItsCityWasBypassed() {
        List<String> owned = List.of("A");
        assertEquals(5, connections.calculateConnectingCost(owned, List.of("B", "C")));
        assertEquals(6, connections.calculateConnectingCost(owned, List.of("C", "B")));
    }

    @Test
    public void treatsFirstCityAsFreeAndChargesForExpansion() {
        assertEquals(0, connections.calculateConnectingCost(List.of(), List.of("Isolated")));
        assertEquals(4, connections.calculateConnectingCost(List.of(), List.of("A", "B")));
    }

    @Test
    public void allowsZeroCostLinksAndEmptyBuildLists() {
        assertEquals(0, connections.calculateConnectingCost(List.of("C"), List.of("D")));
        assertEquals(0, connections.calculateConnectingCost(List.of("C"), List.of()));
        assertEquals(0, connections.calculateConnectingCost(List.of(), List.of()));
    }

    @Test
    public void keepsGraphIndependentOfConstructorInputs() {
        List<String> cities = new ArrayList<>(List.of("A", "B"));
        List<Connections.Link> links = new ArrayList<>(List.of(new Connections.Link("A", "B", 4)));
        Connections graph = new Connections(cities, links);
        cities.clear();
        links.clear();

        assertEquals(4, graph.calculateConnectingCost(List.of("A"), List.of("B")));
    }

    @Test
    public void rejectsUnknownCitiesEvenForAFirstConnection() {
        assertThrows(IllegalArgumentException.class,
                () -> connections.calculateConnectingCost(List.of(), List.of("Unknown")));
        assertThrows(IllegalArgumentException.class,
                () -> connections.calculateConnectingCost(List.of("Unknown"), List.of()));
    }

    @Test
    public void rejectsAlreadyOwnedAndRepeatedTargets() {
        assertThrows(IllegalArgumentException.class,
                () -> connections.calculateConnectingCost(List.of("A"), List.of("A")));
        assertThrows(IllegalArgumentException.class,
                () -> connections.calculateConnectingCost(List.of("A"), List.of("B", "B")));
    }

    @Test
    public void rejectsUnreachableTargets() {
        assertThrows(IllegalArgumentException.class,
                () -> connections.calculateConnectingCost(List.of("A"), List.of("Isolated")));
        assertThrows(IllegalArgumentException.class,
                () -> connections.calculateConnectingCost(List.of(), List.of("Isolated", "B")));
    }

    @Test
    public void rejectsInvalidLinksAtConstruction() {
        assertThrows(NullPointerException.class, () -> new Connections.Link(null, "B", 4));
        assertThrows(NullPointerException.class, () -> new Connections.Link("A", null, 4));
        assertThrows(IllegalArgumentException.class, () -> new Connections.Link(" ", "B", 4));
        assertThrows(IllegalArgumentException.class, () -> new Connections.Link("A", " ", 4));
        assertThrows(IllegalArgumentException.class, () -> new Connections.Link("A", "B", -1));
    }

    @Test
    public void rejectsInvalidGraphData() {
        Connections.Link link = new Connections.Link("A", "B", 4);
        assertThrows(IllegalArgumentException.class, () -> new Connections(List.of(" "), List.of()));
        assertThrows(IllegalArgumentException.class, () -> new Connections(List.of("A"), List.of(link)));
    }

    @Test
    public void rejectsNullGraphDataAndInputs() {
        assertThrows(NullPointerException.class, () -> new Connections(null, List.of()));
        assertThrows(NullPointerException.class, () -> new Connections(List.of(), null));
        assertThrows(NullPointerException.class,
                () -> new Connections(Collections.singletonList(null), List.of()));
        assertThrows(NullPointerException.class,
                () -> new Connections(List.of("A"), Collections.singletonList(null)));
        assertThrows(NullPointerException.class, () -> connections.calculateConnectingCost(null, List.of()));
        assertThrows(NullPointerException.class, () -> connections.calculateConnectingCost(List.of("A"), null));
        assertThrows(NullPointerException.class,
                () -> connections.calculateConnectingCost(List.of("A"), Collections.singletonList(null)));
    }

    @Test
    public void reportsOverflowForLongPathsAndTotals() {
        Connections graph = new Connections(List.of("A", "B", "C"),
                List.of(
                        new Connections.Link("A", "B", Integer.MAX_VALUE),
                        new Connections.Link("B", "C", 1)));

        assertEquals(Integer.MAX_VALUE, graph.calculateConnectingCost(List.of("A"), List.of("B")));
        assertThrows(ArithmeticException.class, () -> graph.calculateConnectingCost(List.of("A"), List.of("C")));
        assertThrows(ArithmeticException.class, () -> graph.calculateConnectingCost(List.of("A"), List.of("B", "C")));
    }
}
