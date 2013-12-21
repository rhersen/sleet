package name.hersen.android.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
            accuracy.setText(textFormatter.getAccuracy(l.getAccuracy(), l.getExtras().get("satellites")));
            speed.setText(textFormatter.getKph(l.getSpeed()));
            direction.setText(textFormatter.getDirection(l.getBearing(), l.getSpeed()));
            bearing.setText(textFormatter.getBearing(l.getBearing(), l.getSpeed()));

            if (l.getSpeed() == 0) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        } catch (Exception e) {
            accuracy.setText(e.toString());
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        accuracy.setText("onStatusChanged: " + status);
    }

    public void onProviderEnabled(String provider) {
        accuracy.setText("Enabled new provider " + provider);
    }

    public void onProviderDisabled(String provider) {
        accuracy.setText("Disabled provider " + provider);
    }
}
