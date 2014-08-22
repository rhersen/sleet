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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class LocationActivity extends Activity implements LocationListener {
    private List<TextView> rows = new ArrayList<TextView>();
    private LocationManager locationManager;
    private String provider;
    private TextFormatter textFormatter = new TextFormatter();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        rows.add((TextView) findViewById(R.id.Row1Text));
        rows.add((TextView) findViewById(R.id.Row2Text));
        rows.add((TextView) findViewById(R.id.Row3Text));
        rows.add((TextView) findViewById(R.id.Row4Text));
        rows.add((TextView) findViewById(R.id.Row5Text));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        rows.get(0).setText(textFormatter.getAccuracy(Float.POSITIVE_INFINITY, 0));
        rows.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rows.get(0).setText("OnClickListener");
            }
        });
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location l) {
        try {
            String server = "sl.hersen.name";
            URL url = new URL("http://" + server + "/nearest?" +
                    "latitude=" + l.getLatitude() +
                    "&longitude=" + l.getLongitude());
            new RequestTask(rows).execute(url);
        } catch (Exception e) {
            rows.get(0).setText(e.toString());
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
        rows.get(0).setText("Enabled new provider " + provider);
    }

    public void onProviderDisabled(String provider) {
        rows.get(0).setText("Disabled provider " + provider);
    }
}

class RequestTask extends AsyncTask<URL, Void, List<StopPoint>> {
    private List<TextView> targets;
    private TextFormatter textFormatter = new TextFormatter();

    public RequestTask(List<TextView> targets) {
        this.targets = targets;
    }

    protected List<StopPoint> doInBackground(URL... params) {
        try {
            URL url = params[0];
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            return textFormatter.parseJson(reader.readLine());
        } catch (Exception e) {
            return emptyList();
        }
    }

    @Override
    protected void onPostExecute(List<StopPoint> s) {
        for (int i = 0; i < targets.size() && i < s.size(); i++) {
            TextView target = targets.get(i);
            StopPoint p = s.get(i);
            target.setText(p.site + " " + p.distance + " " + p.name + " " + p.area);
        }
    }
}