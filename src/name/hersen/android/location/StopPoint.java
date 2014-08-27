package name.hersen.android.location;

import org.json.JSONException;
import org.json.JSONObject;

public class StopPoint {
    public int distance;
    public String name;
    public String site;
    public String area;

    public StopPoint(int distance, String name, String site, String area) {
        this.distance = distance;
        this.name = name;
        this.site = site;
        this.area = area;
    }

    public StopPoint(JSONObject json) throws JSONException {
        this(json.getInt("distance"), json.getString("name"), json.getString("site"), json.getString("area"));
    }
}
