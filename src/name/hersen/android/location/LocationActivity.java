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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

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

    public void onLocationChanged(Location l) {
        try {
            new GetNearest(rows).execute(l);
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
    private TextFormatter textFormatter = new TextFormatter();

    GetNearest(List<Row> targets) {
        this.targets = targets;
    }

    protected List<StopPoint> doInBackground(Location... params) {
        try {
            Location l = params[0];
            URL url = getUrl(l);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            return textFormatter.parseJson(reader.readLine());
        } catch (Exception e) {
            return emptyList();
        }
    }

    private URL getUrl(Location l) throws MalformedURLException {
        return new URL("http://" + "sl.hersen.name" +
                "/nearest?latitude=" + l.getLatitude() + "&longitude=" + l.getLongitude());
    }

    protected void onPostExecute(List<StopPoint> s) {
        for (int i = 0; i < targets.size() && i < s.size(); i++) {
            targets.get(i).setStopPoint(s.get(i));
        }
    }
}

class Row implements View.OnClickListener {
    private final TextView view;
    private StopPoint stopPoint;
    private TextView status;

    Row(View view, View status) {
        this.view = (TextView) view;
        this.status = (TextView) status;
        View.OnClickListener listener = this;
        view.setOnClickListener(listener);
    }

    public void onClick(View v) {
        status.setText(stopPoint.site);
    }

    void setStopPoint(StopPoint p) {
        stopPoint = p;
        view.setText(p.site + " " + p.distance + " " + p.name + " " + p.area);
    }
}