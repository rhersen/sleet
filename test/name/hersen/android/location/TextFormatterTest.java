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
}
