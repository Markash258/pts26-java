package sk.uniba.fmph.dcs.power_grid;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class RequestErrorTest {

    @Test
    public void preservesDescription() {
        final RequestError error = new RequestError("Not enough money ");

        assertEquals("Not enough money ", error.getDescription());
    }

    @Test
    public void equalDescriptionsHaveEqualValuesAndHashCodes() {
        final RequestError first = new RequestError("Not enough money");
        final RequestError second = new RequestError("Not enough money");

        assertEquals(first, second);
        assertEquals(second, first);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void differsFromOtherDescriptionsAndTypes() {
        final RequestError error = new RequestError("Not enough money");

        assertNotEquals(error, new RequestError("Not enough fuel"));
        assertNotEquals(error, null);
        assertNotEquals(error, "Not enough money");
    }

    @Test
    public void stringRepresentationContainsDescription() {
        final RequestError error = new RequestError("Not enough money");

        assertEquals("Not enough money", error.toString());
    }

    @Test
    public void rejectsNullDescription() {
        assertThrows(NullPointerException.class, () -> new RequestError(null));
    }

    @Test
    public void rejectsEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () -> new RequestError(""));
    }

    @Test
    public void rejectsWhitespaceOnlyDescription() {
        assertThrows(IllegalArgumentException.class, () -> new RequestError("   "));
        assertThrows(IllegalArgumentException.class, () -> new RequestError("\t\n"));
    }
}
