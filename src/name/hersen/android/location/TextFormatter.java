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

    int getImageResource(double latitude, double longitude) {
        Integer[] row0 = {
                R.drawable.t0,
                R.drawable.t1,
                R.drawable.t2,
                R.drawable.t3,
                R.drawable.t4,
                R.drawable.t5,
                R.drawable.t6
        };
        Integer[] row1 = {
                R.drawable.t7,
                R.drawable.t8,
                R.drawable.t9,
                R.drawable.t10,
                R.drawable.t11,
                R.drawable.t12,
                R.drawable.t13,
        };
        Integer[] row2 = {
                R.drawable.t14,
                R.drawable.t15,
                R.drawable.t16,
                R.drawable.t17,
                R.drawable.t18,
                R.drawable.t19,
                R.drawable.t20,
        };
        Integer[] row3 = {
                R.drawable.t21,
                R.drawable.t22,
                R.drawable.t23,
                R.drawable.t24,
                R.drawable.t25,
                R.drawable.t26,
                R.drawable.t27,
        };
        Integer[] row4 = {
                R.drawable.t28,
                R.drawable.t29,
                R.drawable.t30,
                R.drawable.t31,
                R.drawable.t32,
                R.drawable.t33,
                R.drawable.t34,
        };
        Integer[] row5 = {
                R.drawable.t35,
                R.drawable.t36,
                R.drawable.t37,
                R.drawable.t38,
                R.drawable.t39,
                R.drawable.t40,
                R.drawable.t41,
        };
        Integer[] row6 = {
                R.drawable.t42,
                R.drawable.t43,
                R.drawable.t44,
                R.drawable.t45,
                R.drawable.t46,
                R.drawable.t47,
                R.drawable.t48,
        };
        Integer[] row7 = {
                R.drawable.t49,
                R.drawable.t50,
                R.drawable.t51,
                R.drawable.t52,
                R.drawable.t53,
                R.drawable.t54,
                R.drawable.t55,
        };
        Integer[] row8 = {
                R.drawable.t56,
                R.drawable.t57,
                R.drawable.t58,
                R.drawable.t59,
                R.drawable.t60,
                R.drawable.t61,
                R.drawable.t62,
        };
        Integer[] row9 = {
                R.drawable.t63,
                R.drawable.t64,
                R.drawable.t65,
                R.drawable.t66,
                R.drawable.t67,
                R.drawable.t68,
                R.drawable.t69,
        };
        Integer[] row10 = {
                R.drawable.t70,
                R.drawable.t71,
                R.drawable.t72,
                R.drawable.t73,
                R.drawable.t74,
                R.drawable.t75,
                R.drawable.t76
        };
        Integer[][] rows = {
                row0,
                row1,
                row2,
                row3,
                row4,
                row5,
                row6,
                row7,
                row8,
                row9,
                row10,
        };

        return rows[getRow(latitude)][getColumn(longitude)];
    }

    int getRow(double v) {
        double d = (59.228 - v) * 205;
        return (int) d;
    }

    int getColumn(double v) {
        double d = (v - 17.875) * 110;
        return (int) d;
    }
}
