package name.hersen.android.location;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextFormatterTest {

    private TextFormatter subject;

    @Before
    public void setUp()  {
        subject = new TextFormatter();
    }

    @Test
    public void mps()  {
        String result = subject.getMetersPerSecond(2.5f);
        assertEquals("2.5 m/s", result);
    }

    @Test
    public void kph()  {
        String result = subject.getKph(1.5f);
        assertEquals("5 km/h", result);
    }

    @Test
    public void bearing()  {
        String result = subject.getBearing(28.12f);
        assertEquals("28.12°", result);
    }

    @Test
    public void mainDirections()  {
        assertEquals("N", subject.getDirection(0f));
        assertEquals("E", subject.getDirection(90f));
        assertEquals("S", subject.getDirection(180f));
        assertEquals("W", subject.getDirection(270f));
    }

    @Test
    public void approximateDirection()  {
        assertEquals("E", subject.getDirection(100f));
    }

    @Test
    public void approximateDirectionPastWest()  {
        assertEquals("N", subject.getDirection(350f));
    }
}
