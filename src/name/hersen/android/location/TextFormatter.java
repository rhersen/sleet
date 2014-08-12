package name.hersen.android.location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFormatter {

    private final char[] directions = new char[]{'N', 'E', 'S', 'W'};

    String getMetersPerSecond(float speed) {
        return speed + " m/s";
    }

    public String getBearing(float bearing, float speed) {
        if (speed == 0) {
            return "";
        }

        return bearing + "°";
    }

    public String getKph(float speed) {
        return Math.round(3.6f * speed) + " km/h";
    }

    public String getDirection(float bearing, float speed) {
        if (speed == 0) {
            return "";
        }

        if (Math.round(bearing / 45) % 2 == 0) {
            return getMainDirection(bearing);
        }

        return getDiagonalDirection(bearing);
    }

    private String getMainDirection(float bearing) {
        int i = Math.round(bearing / 90);
        return "" + directions[i % 4];
    }

    private String getDiagonalDirection(float bearing) {
        int northOrSouth = 2 * (Math.round(bearing / 180) % 2);
        int eastOrWest = (2 * Math.round((bearing + 90) / 180) + 3) % 4;
        return directions[northOrSouth] + "" + directions[eastOrWest];
    }

    String getAccuracy(float accuracy, Object satellites) {
        StringBuilder s = new StringBuilder();

        s.append(satellites);
        s.append(" satellites");

        if (!Float.isInfinite(accuracy)) {
            s.append(" (");

            int round = Math.round(accuracy);

            if (round == accuracy) {
                s.append(round);
            } else {
                s.append(accuracy);
            }

            s.append(" m)");
        }

        return s.toString();
    }

    int getRow(double v) {
        double d = (59.228 - v) * 205;
        return (int) d;
    }

    int getColumn(double v) {
        double d = (v - 17.875) * 110;
        return (int) d;
    }

    public List<String> parseDistance(String s) {
        try {
            ArrayList<String> r = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                r.add(json.getInt("distance") + " " + json.getString("name"));
            }
            return r;
        } catch (Exception e) {
            return Collections.singletonList(s);
        }
    }
}
