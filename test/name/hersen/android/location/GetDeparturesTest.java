package name.hersen.android.location;

import org.apache.tools.ant.util.ReaderInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLConnection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class GetDeparturesTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testTrains() throws Exception {
        String json = "{\"trains\":[{\"JourneyDirection\":1,\"SecondaryDestinationName\":null,\"StopAreaName\":\"Järna\",\"StopAreaNumber\":5251,\"StopPointNumber\":5251,\"StopPointDesignation\":\"2\",\"TimeTabledDateTime\":\"2014-09-01T22:07:00\",\"ExpectedDateTime\":\"2014-09-01T22:07:00\",\"DisplayTime\":\"10 min\",\"Deviations\":null,\"TransportMode\":\"TRAIN\",\"LineNumber\":\"37\",\"Destination\":\"Gnesta\",\"SiteId\":9542},{\"JourneyDirection\":2,\"SecondaryDestinationName\":null,\"StopAreaName\":\"Järna\",\"StopAreaNumber\":5251,\"StopPointNumber\":5252,\"StopPointDesignation\":\"3\",\"TimeTabledDateTime\":\"2014-09-01T22:50:00\",\"ExpectedDateTime\":\"2014-09-01T22:50:00\",\"DisplayTime\":\"22:50\",\"Deviations\":null,\"TransportMode\":\"TRAIN\",\"LineNumber\":\"37\",\"Destination\":\"Södertälje C\",\"SiteId\":9542}],\"StopAreaName\":\"Järna\",\"SiteId\":9542}";
        GetDepartures subject = new GetDepartures(null, connecterMock(connectionMock(json)));

        String result = subject.doInBackground("9542");

        assertThat(result, is("5251 22:07:00 Gnesta"));
    }

    @Test
    public void testBuses() throws Exception {
        String json = "{\"buses\":[{\"JourneyDirection\":1,\"SecondaryDestinationName\":null,\"StopAreaName\":\"Järna\",\"StopAreaNumber\":5251,\"StopPointNumber\":5251,\"StopPointDesignation\":\"2\",\"TimeTabledDateTime\":\"2014-09-01T22:07:00\",\"ExpectedDateTime\":\"2014-09-01T22:07:00\",\"DisplayTime\":\"10 min\",\"Deviations\":null,\"TransportMode\":\"TRAIN\",\"LineNumber\":\"37\",\"Destination\":\"Gnesta\",\"SiteId\":9542},{\"JourneyDirection\":2,\"SecondaryDestinationName\":null,\"StopAreaName\":\"Järna\",\"StopAreaNumber\":5251,\"StopPointNumber\":5252,\"StopPointDesignation\":\"3\",\"TimeTabledDateTime\":\"2014-09-01T22:50:00\",\"ExpectedDateTime\":\"2014-09-01T22:50:00\",\"DisplayTime\":\"22:50\",\"Deviations\":null,\"TransportMode\":\"TRAIN\",\"LineNumber\":\"37\",\"Destination\":\"Södertälje C\",\"SiteId\":9542}],\"StopAreaName\":\"Järna\",\"SiteId\":9542}";
        GetDepartures subject = new GetDepartures(null, connecterMock(connectionMock(json)));

        String result = subject.doInBackground("9542");

        assertThat(result, is("5251 22:07:00 Gnesta"));
    }

    @Test
    public void testNoTrains() throws Exception {
        String json = "{\"trains\":[]}";
        GetDepartures subject = new GetDepartures(null, connecterMock(connectionMock(json)));

        String result = subject.doInBackground("9542");

        assertThat(result, is("no departures"));
    }

    private URLConnection connectionMock(String json) throws IOException {
        URLConnection mock = mock(URLConnection.class);
        when(mock.getInputStream()).thenReturn(new ReaderInputStream(new StringReader(json)));
        return mock;
    }

    private Connecter connecterMock(URLConnection urlConnection) throws IOException {
        Connecter mock = mock(Connecter.class);
        when(mock.getConnection(anyString())).thenReturn(urlConnection);
        return mock;
    }

    @Test
    public void testOnPostExecute() throws Exception {

    }
}