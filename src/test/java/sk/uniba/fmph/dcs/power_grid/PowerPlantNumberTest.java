package sk.uniba.fmph.dcs.power_grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class PowerPlantNumberTest {

    @Test
    public void storesNumberPassedToConstructor() {
        final PowerPlantNumber number = new PowerPlantNumber(17);

        assertEquals(17, number.getNumber());
    }

    @Test
    public void acceptsSmallestPositiveNumber() {
        assertEquals(1, new PowerPlantNumber(1).getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsZero() {
        new PowerPlantNumber(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNegativeNumber() {
        new PowerPlantNumber(-1);
    }

    @Test
    public void equalNumbersCanBeUsedAsMapKeys() {
        final Map<PowerPlantNumber, String> plants = new HashMap<>();
        plants.put(new PowerPlantNumber(17), "owned plant");

        assertEquals("owned plant", plants.get(new PowerPlantNumber(17)));
    }

    @Test
    public void differentNumbersAreNotEqual() {
        assertNotEquals(new PowerPlantNumber(17), new PowerPlantNumber(18));
    }

    @Test
    public void doesNotEqualNullOrAnotherType() {
        final PowerPlantNumber number = new PowerPlantNumber(17);

        assertFalse(number.equals(null));
        assertFalse(number.equals(Integer.valueOf(17)));
    }

    @Test
    public void sortsByNumericValue() {
        final List<PowerPlantNumber> numbers = new ArrayList<>(List.of(
                new PowerPlantNumber(50),
                new PowerPlantNumber(3),
                new PowerPlantNumber(17)));

        Collections.sort(numbers);

        assertEquals(List.of(
                new PowerPlantNumber(3),
                new PowerPlantNumber(17),
                new PowerPlantNumber(50)), numbers);
    }

    @Test
    public void stringRepresentationContainsNumber() {
        assertEquals("17", new PowerPlantNumber(17).toString());
    }
}
