package name.hersen.android.location;

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
        String s = satellites + " satellites";

        if (Float.isInfinite(accuracy)) {
            return s;
        }

        return s + " (" + accuracy + " m)";
    }
}
