package name.hersen.android.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class LocationActivity extends Activity implements LocationListener {
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private LocationManager locationManager;
    private String provider;
    private TextFormatter textFormatter = new TextFormatter();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text1 = (TextView) findViewById(R.id.TextView1);
        text2 = (TextView) findViewById(R.id.TextView2);
        text3 = (TextView) findViewById(R.id.TextView3);
        text4 = (TextView) findViewById(R.id.TextView4);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        text1.setText("wait...");
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
            text1.setText(l.getExtras().get("satellites") + " satellites (" + l.getAccuracy() + " m)");
            text2.setText(textFormatter.getKph(l.getSpeed()));
            text3.setText(textFormatter.getDirection(l.getBearing(), l.getSpeed()));
            text4.setText(textFormatter.getBearing(l.getBearing(), l.getSpeed()));
        } catch (Exception e) {
            text1.setText(e.toString());
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        text1.setText("onStatusChanged: " + status);
    }

    public void onProviderEnabled(String provider) {
        text1.setText("Enabled new provider " + provider);
    }

    public void onProviderDisabled(String provider) {
        text1.setText("Disabled provider " + provider);
    }
}
