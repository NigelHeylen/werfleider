package nigel.com.werfleider.util;

/**
 * Created by nigel on 14/03/15.
 */
public enum MeasuringUnit {
    MM(0), MM2(1), MM3(2),
    CM(0), CM2(1), CM3(2),
    DM(0), DM2(1), DM3(2),
    M(0), M2(1), M3(2);

    final int weight;

    MeasuringUnit(final int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
