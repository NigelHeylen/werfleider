package nigel.com.werfleider.model;

import com.google.common.base.Predicate;

import java.util.List;

import nigel.com.werfleider.util.MeasuringUnit;

import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 04/12/14.
 */
public class DocumentLocation {

    private int id;
    private int documentId;
    private String location;
    private List<DocumentImage> imageList;
    private MeasuringUnit measuringUnit;

    public DocumentLocation(final String location) {
        this.location = location;
        imageList = newArrayList();
    }

    public DocumentLocation(final String location, final List<DocumentImage> imageList) {
        this.location = location;
        this.imageList = imageList;
    }

    public int getId() {
        return id;
    }

    public DocumentLocation setId(final int id) {
        this.id = id;
        return this;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final int documentId) {
        this.documentId = documentId;
    }

    public String getLocation() {
        return location;
    }

    public List<DocumentImage> getImageList() {
        return imageList;
    }

    public void addToImageList(final DocumentImage image){
        if(!any(imageList, new Predicate<DocumentImage>() {
                    @Override public boolean apply(final DocumentImage input) {
                        return input.getImageURL().equals(image.getImageURL());
                    }
                })) {
            imageList.add(image);
        }
    }

    public DocumentLocation setImageList(final List<DocumentImage> imageList) {
        this.imageList = imageList;
        return this;

    }

    public boolean hasImages(){
        return !isEmpty(imageList);
    }

    public DocumentLocation setLocation(final String location) {
        this.location = location;
        return this;

    }

    public MeasuringUnit getMeasuringUnit() {
        return measuringUnit;
    }

    public DocumentLocation setMeasuringUnit(final MeasuringUnit measuringUnit) {
        this.measuringUnit = measuringUnit;
        return this;
    }
}
