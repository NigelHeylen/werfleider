package nigel.com.werfleider.pdf;

/**
 * Created by nigel on 04/06/15.
 */
public class FloorLocationDataHolder implements Comparable<FloorLocationDataHolder>{

    final String location;

    final String floor;

    public FloorLocationDataHolder(final String location, final String floor) {

        this.location = location;
        this.floor = floor;
    }

    public String getLocation() {

        return location;
    }

    public String getFloor() {

        return floor;
    }

    @Override public int compareTo(final FloorLocationDataHolder another) {

        return this.floor.compareTo(another.floor);
    }
}
