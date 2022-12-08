package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ResourceTest {

    Resource resource = new Resource(4, 2, 8);

    @Test
    public void testResource() {
        // Test if the values are stored correctly
        assertEquals(4, resource.getCpu());
        assertEquals(2, resource.getGpu());
        assertEquals(8, resource.getMemory());
    }

    @Test
    public void testGetCpu() {
        assertEquals(4, resource.getCpu());
    }

    @Test
    public void testGetGpu() {
        assertEquals(2, resource.getGpu());
    }

    @Test
    public void testGetMemory() {
        assertEquals(8, resource.getMemory());
    }
}
