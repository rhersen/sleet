package name.hersen.android.location;

import android.location.Location;
import org.apache.tools.ant.util.ReaderInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.StringReader;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class GetNearestTest {

    private GetNearest subject;

    @Before
    public void setUp() throws Exception {
        Connecter connecter = mock(Connecter.class);
        URLConnection urlConnection = mock(URLConnection.class);
        when(urlConnection.getInputStream()).thenReturn(new ReaderInputStream(new StringReader("")));
        when(connecter.getConnection((Location) anyObject())).thenReturn(urlConnection);
        subject = new GetNearest(Collections.<Row>emptyList(), connecter);
    }

    @Test
    public void testDoInBackground() throws Exception {
        List<StopPoint> result = subject.doInBackground(mock(Location.class));
        assertThat(result.size(), is(1));
    }

    @Test
    public void testOnPostExecute() throws Exception {

    }
}