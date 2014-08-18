package name.hersen.android.location;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class TextFormatter {

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

    public List<String> parseJson(String s) {
        try {
            List<String> r = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                r.add(json.getInt("distance") + " " + json.getString("name"));
            }
            return r;
        } catch (Exception e) {
            return singletonList(s);
        }
    }
}
