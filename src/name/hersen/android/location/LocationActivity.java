package name.hersen.android.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class LocationActivity extends Activity implements LocationListener {
    private TextView status;
    private List<Row> rows = new ArrayList<Row>();
    private LocationManager locationManager;
    private String provider;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        status = (TextView) findViewById(R.id.StatusText);

        rows.add(getRow(R.id.Row1Text));
        rows.add(getRow(R.id.Row2Text));
        rows.add(getRow(R.id.Row3Text));
        rows.add(getRow(R.id.Row4Text));
        rows.add(getRow(R.id.Row5Text));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        status.setText("This is sleet.");
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    private Row getRow(final int id) {
        return new Row(findViewById(id), status);
    }

    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void onLocationChanged(Location location) {
        try {
            new GetNearest(rows, new Connecter()).execute(location);
        } catch (Exception e) {
            status.setText(e.toString());
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
        status.setText("Enabled new provider " + provider);
    }

    public void onProviderDisabled(String provider) {
        status.setText("Disabled provider " + provider);
    }
}

class GetNearest extends AsyncTask<Location, Void, List<StopPoint>> {
    private List<Row> targets;
    private Connecter connecter;

    GetNearest(List<Row> targets, Connecter connecter) {
        this.targets = targets;
        this.connecter = connecter;
    }

    protected List<StopPoint> doInBackground(Location... locations) {
        try {
            URLConnection con = connecter.getConnection(locations[0]);
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            try {
                List<StopPoint> r = new ArrayList<StopPoint>();
                JSONArray jsonArray = new JSONArray(reader.readLine());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    r.add(new StopPoint(json));
                }
                return r;
            } catch (Exception e) {
                return singletonList(null);
            }
        } catch (MalformedURLException e) {
            return emptyList();
        } catch (IOException e) {
            return emptyList();
        }
    }

    protected void onPostExecute(List<StopPoint> s) {
        for (int i = 0; i < targets.size() && i < s.size(); i++) {
            targets.get(i).setStopPoint(s.get(i));
        }
    }
}

class Connecter {
    URLConnection getConnection(Location l) throws IOException {
        String server = "sl.hersen.name";
        URL url = new URL("http://" + server + "/nearest" +
                "?latitude=" + l.getLatitude() +
                "&longitude=" + l.getLongitude());
        return url.openConnection();
    }
}

class Row implements View.OnClickListener {
    private final TextView text;
    private StopPoint stopPoint;
    private TextView status;

    Row(View text, View status) {
        this.text = (TextView) text;
        this.status = (TextView) status;
        View.OnClickListener listener = this;
        text.setOnClickListener(listener);
    }

    public void onClick(View v) {
        new GetDepartures(status).execute(stopPoint.site);
    }

    void setStopPoint(StopPoint p) {
        stopPoint = p;
        text.setText(p.site + " " + p.distance + " " + p.name + " " + p.area);
    }
}

class GetDepartures extends AsyncTask<String, Void, String> {
    private TextView status;

    public GetDepartures(TextView status) {
        this.status = status;
    }

    protected void onPostExecute(String s) {
        status.setText(s);
    }

    protected String doInBackground(String... params) {
        return params[0];
    }
}