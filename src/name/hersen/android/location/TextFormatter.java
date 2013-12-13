package name.hersen.android.location;

public class TextFormatter {
    String getMetersPerSecond(float speed) {
        return speed + " m/s";
    }

    public String getBearing(float bearing) {
        return bearing + "°";
    }

    public String getKph(float speed) {
        return Math.round(3.6f * speed) + " km/h";
    }

    public String getDirection(float bearing) {
        char[] directions = {'N', 'E', 'S', 'W'};
        int i = Math.round(bearing / 90);
        return "" + directions[i % 4];
    }
}
