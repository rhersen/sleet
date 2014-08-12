package name.hersen.android.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class LocationActivity extends Activity implements LocationListener {
    private TextView row1;
    private TextView row2;
    private TextView row3;
    private TextView row4;
    private TextView row5;
    private LocationManager locationManager;
    private String provider;
    private TextFormatter textFormatter = new TextFormatter();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        row1 = (TextView) findViewById(R.id.AccuracyText);
        row2 = (TextView) findViewById(R.id.SpeedText);
        row3 = (TextView) findViewById(R.id.DirectionText);
        row4 = (TextView) findViewById(R.id.BearingText);
        row5 = (TextView) findViewById(R.id.Row5Text);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        row1.setText(textFormatter.getAccuracy(Float.POSITIVE_INFINITY, 0));
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
            String server = "sl.hersen.name:3000";
            URL url = new URL("http://" + server + "/nearest?latitude=" + l.getLatitude() + "&longitude=" + l.getLongitude());
            new RequestTask(row1, row2, row3, row4, row5).execute(url);
        } catch (Exception e) {
            row1.setText(e.toString());
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
        row1.setText("Enabled new provider " + provider);
    }

    public void onProviderDisabled(String provider) {
        row1.setText("Disabled provider " + provider);
    }
}

class RequestTask extends AsyncTask<URL, Void, List<String>> {
    private TextView[] targets;
    private TextFormatter textFormatter = new TextFormatter();

    public RequestTask(TextView... targets) {
        this.targets = targets;
    }

    protected List<String> doInBackground(URL... params) {
        try {
            URL url = params[0];
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            return textFormatter.parseDistance(reader.readLine());
        } catch (Exception e) {
            return Collections.singletonList(e.toString());
        }
    }

    @Override
    protected void onPostExecute(List<String> s) {
        int n = targets.length < s.size() ? targets.length : s.size();
        for (int i = 0; i < n; i++) {
            TextView target = targets[i];
            target.setText(s.get(i));
        }
    }
}