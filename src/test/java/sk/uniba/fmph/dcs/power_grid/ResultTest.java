package sk.uniba.fmph.dcs.power_grid;

import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class ResultTest {

    @Test
    public void zeroIsASuccessfulIntegerValue() {
        final Result<Integer> result = Result.success(0);

        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertEquals(Integer.valueOf(0), result.getValue());
    }

    @Test
    public void supportsStringValues() {
        final Result<String> result = Result.success("serialized state");

        assertEquals("serialized state", result.getValue());
    }

    @Test
    public void preservesATypedResourceList() {
        final List<Resource> resources = List.of(Resource.COAL, Resource.OIL);
        final Result<List<Resource>> result = Result.success(resources);

        assertSame(resources, result.getValue());
    }

    @Test
    public void failurePreservesTheRequestError() {
        final RequestError error = new RequestError("Not enough money");
        final Result<Integer> result = Result.failure(error);

        assertTrue(result.isFailure());
        assertFalse(result.isSuccess());
        assertSame(error, result.getError());
    }

    @Test
    public void rejectsNullSuccessValues() {
        assertThrows(NullPointerException.class, () -> Result.success(null));
    }

    @Test
    public void rejectsNullErrors() {
        assertThrows(NullPointerException.class, () -> Result.failure(null));
    }

    @Test
    public void failureCannotBeReadAsASuccess() {
        final Result<Integer> result = Result.failure(new RequestError("Rejected"));

        assertThrows(IllegalStateException.class, result::getValue);
    }

    @Test
    public void successCannotBeReadAsAFailure() {
        final Result<Integer> result = Result.success(15);

        assertThrows(IllegalStateException.class, result::getError);
    }


    @Test
    public void propagatesAnErrorBetweenDifferentSuccessTypes() {
        final Result<Integer> calculation = Result.failure(new RequestError("Not enough fuel"));
        final Result<String> response = Result.failure(calculation.getError());

        assertTrue(response.isFailure());
        assertSame(calculation.getError(), response.getError());
    }
}
