package name.hersen.android.location;

import android.location.Location;
import org.apache.tools.ant.util.ReaderInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
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

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testDoInBackground() throws Exception {
        String json = "[{\"name\":\"Tullinge centrum\",\"area\":\"72332\",\"site\":\"7219\",\"distance\":677}]";
        GetNearest subject = new GetNearest(Collections.<Row>emptyList(), connecterMock(connectionMock(json)));

        List<StopPoint> results = subject.doInBackground(mock(Location.class));

        assertThat(results.size(), is(1));
        StopPoint result = results.get(0);
        assertThat(result.name, is("Tullinge centrum"));
    }

    private URLConnection connectionMock(String json) throws IOException {
        URLConnection mock = mock(URLConnection.class);
        when(mock.getInputStream()).thenReturn(new ReaderInputStream(new StringReader(json)));
        return mock;
    }

    private Connecter connecterMock(URLConnection urlConnection) throws IOException {
        Connecter mock = mock(Connecter.class);
        when(mock.getConnection((Location) anyObject())).thenReturn(urlConnection);
        return mock;
    }

    @Test
    public void testOnPostExecute() throws Exception {

    }
}