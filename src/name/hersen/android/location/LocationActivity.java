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

public class LocationActivity extends Activity implements LocationListener {
    private TextView accuracy;
    private TextView speed;
    private TextView direction;
    private TextView bearing;
    private LocationManager locationManager;
    private String provider;
    private TextFormatter textFormatter = new TextFormatter();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        accuracy = (TextView) findViewById(R.id.AccuracyText);
        speed = (TextView) findViewById(R.id.SpeedText);
        direction = (TextView) findViewById(R.id.DirectionText);
        bearing = (TextView) findViewById(R.id.BearingText);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        accuracy.setText(textFormatter.getAccuracy(Float.POSITIVE_INFINITY, 0));
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
            speed.setText(textFormatter.getKph(l.getSpeed()));
            direction.setText("lat: " + l.getLatitude());
            bearing.setText("lon: " + l.getLongitude());
            String server = "sl.hersen.name:3000";
            URL url = new URL("http://" + server + "/nearest?latitude=" + l.getLatitude() + "&longitude=" + l.getLongitude());
            new RequestTask(accuracy).execute(url);
        } catch (Exception e) {
            accuracy.setText(e.toString());
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
        accuracy.setText("Enabled new provider " + provider);
    }

    public void onProviderDisabled(String provider) {
        accuracy.setText("Disabled provider " + provider);
    }
}

class RequestTask extends AsyncTask<URL, Void, String> {
    private TextView target;
    private TextFormatter textFormatter = new TextFormatter();

    public RequestTask(TextView target) {
        this.target = target;
    }

    protected String doInBackground(URL... params) {
        try {
            URL url = params[0];
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            return textFormatter.parseDistance(reader.readLine());
        } catch (Exception e) {
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        target.setText(s);
    }
}