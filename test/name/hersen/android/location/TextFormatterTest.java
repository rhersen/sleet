package name.hersen.android.location;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextFormatterTest {

    private TextFormatter subject;

    @Before
    public void setUp() {
        subject = new TextFormatter();
    }

    @Test
    public void mps() {
        String result = subject.getMetersPerSecond(2.5f);
        assertEquals("2.5 m/s", result);
    }

    @Test
    public void kph() {
        String result = subject.getKph(1.5f);
        assertEquals("5 km/h", result);
    }

    @Test
    public void bearing() {
        assertEquals("28.12°", subject.getBearing(28.12f, .5f));
    }

    @Test
    public void bearingForZeroDegreesIfSpeedIsGreaterThanZero() {
        assertEquals("0.0°", subject.getBearing(0, .5f));
    }

    @Test
    public void mainDirections() {
        assertEquals("N", subject.getDirection(0f, 0.5f));
        assertEquals("E", subject.getDirection(90f, 0.5f));
        assertEquals("S", subject.getDirection(180f, 0.5f));
        assertEquals("W", subject.getDirection(270f, 0.5f));
    }

    @Test
    public void approximateDirection() {
        assertEquals("E", subject.getDirection(100f, 0.5f));
    }

    @Test
    public void approximateDirectionPastWest() {
        assertEquals("N", subject.getDirection(350f, 0.5f));
    }

    @Test
    public void diagonalDirections() {
        assertEquals("NE", subject.getDirection(45f, 0.5f));
        assertEquals("SE", subject.getDirection(135f, 0.5f));
    }

    @Test
    public void northWest() {
        assertEquals("NW", subject.getDirection(330f, 0.5f));
    }

    @Test
    public void standingStill() {
        assertEquals("", subject.getDirection(0f, 0f));
        assertEquals("", subject.getBearing(0f, 0f));
    }

    @Test
    public void accuracy() {
        assertEquals("9 satellites (7.5 m)", subject.getAccuracy(7.5f, 9));
    }

    @Test
    public void shouldNotAddDotZero() {
        assertEquals("9 satellites (8 m)", subject.getAccuracy(8f, 9));
    }

    @Test
    public void accuracyBeforeGpsFix() {
        assertEquals("0 satellites", subject.getAccuracy(Float.POSITIVE_INFINITY, 0));
    }

    @Test
    public void getColumn() {
        assertEquals(0, subject.getColumn(17.88));
        assertEquals(0, subject.getColumn(17.8804));
        assertEquals(1, subject.getColumn(17.8842));
        assertEquals(1, subject.getColumn(17.89));
        assertEquals(5, subject.getColumn(17.921));
        assertEquals(5, subject.getColumn(17.9274));
        assertEquals(6, subject.getColumn(17.932));
        assertEquals(6, subject.getColumn(17.931));
    }

    @Test
    public void getRow() {
        assertEquals(0, subject.getRow(59.225));
        assertEquals(0, subject.getRow(59.2235));
        assertEquals(1, subject.getRow(59.2224));
        assertEquals(3, subject.getRow(59.21));
        assertEquals(5, subject.getRow(59.2));
        assertEquals(6, subject.getRow(59.194));
        assertEquals(9, subject.getRow(59.1804));
        assertEquals(10, subject.getRow(59.179));
        assertEquals(10, subject.getRow(59.177));
    }

    @Test
    public void parseOneDistance() {
        assertEquals("", subject.parseDistance(""));
        assertEquals("1 n", subject.parseDistance("[{\"name\":\"n\",\"distance\":1}]"));
        assertEquals("1 X", subject.parseDistance("[{\"name\":\"X\",\"distance\":1}]"));
        assertEquals("2 n", subject.parseDistance("[{\"name\":\"n\",\"distance\":2}]"));
    }

}
