package nigel.com.werfleider.model;

/**
 * Created by nigel on 08/12/14.
 */
public class CreateImage {

    private final String currentUri;
    private final int imageLocationIndex;

    public CreateImage(final String currentUri, final int imageLocationIndex) {
        this.currentUri = currentUri;
        this.imageLocationIndex = imageLocationIndex;
    }

    public int getImageLocationIndex() {
        return imageLocationIndex;
    }

    public String getCurrentUri() {
        return currentUri;
    }
}
