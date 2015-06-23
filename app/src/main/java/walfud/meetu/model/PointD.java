package walfud.meetu.model;

/**
 * Created by song on 2015/6/21.
 */
public class PointD {
    private double x;
    private double y;

    public PointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }

        PointD other = (PointD) o;
        return x == other.x && y == other.y;
    }
}
