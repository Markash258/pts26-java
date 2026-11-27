package sk.uniba.fmph.dcs.power_grid;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SerializedGameStateTest {

    @Test
    public void notNullGameStateTest() {
        assertThrows(AssertionError.class, () -> new SerializedGameState(null));
    }

    @Test
    public void storesStatePassedToConstructor() {
        assertEquals("Lorem Ipsum", new SerializedGameState("Lorem Ipsum").getState());
    }

    @Test
    public void equalsStatePassedToConstructor() {
        SerializedGameState a = new SerializedGameState("123");
        SerializedGameState b = new SerializedGameState("Iny state");
        String st = "123";
        int n = 123;
        SerializedGameState matchA = new SerializedGameState("123");

        ArrayList<Boolean> out = new ArrayList<>();
        out.add(a.equals(a)); // True
        out.add(a.equals(b)); // False
        out.add(a.equals(st)); // False
        out.add(a.equals(n)); // False
        out.add(a.equals(null)); // False
        out.add(a.equals(matchA)); // True
        out.add(b.equals(matchA)); // False

        List<Boolean> expectedOut = List.of(true, false, false, false, false, true, false);
        assertEquals(expectedOut, out);
    }

    @Test
    public void hashCodeMatchesForEqualStates() {
        SerializedGameState a = new SerializedGameState("123");
        SerializedGameState matchA = new SerializedGameState("123");

        assertEquals(a.hashCode(), matchA.hashCode());
    }

    @Test
    public void toStringContainsState() {
        SerializedGameState a = new SerializedGameState("123");

        assertTrue(a.toString().contains("123"));
    }
}
