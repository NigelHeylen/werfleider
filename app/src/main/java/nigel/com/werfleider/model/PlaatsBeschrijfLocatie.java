package nigel.com.werfleider.model;

import com.google.common.base.Predicate;

import java.util.List;

import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 04/12/14.
 */
public class PlaatsBeschrijfLocatie {

    private int id;
    private int plaatsbeschrijfId;
    private String location;
    private List<PlaatsBeschrijfImage> imageList;

    public PlaatsBeschrijfLocatie(final String location) {
        this.location = location;
        imageList = newArrayList();
    }

    public PlaatsBeschrijfLocatie(final String location, final List<PlaatsBeschrijfImage> imageList) {
        this.location = location;
        this.imageList = imageList;
    }

    public int getId() {
        return id;
    }

    public PlaatsBeschrijfLocatie setId(final int id) {
        this.id = id;
        return this;
    }

    public int getPlaatsbeschrijfId() {
        return plaatsbeschrijfId;
    }

    public void setPlaatsbeschrijfId(final int plaatsbeschrijfId) {
        this.plaatsbeschrijfId = plaatsbeschrijfId;
    }

    public String getLocation() {
        return location;
    }

    public List<PlaatsBeschrijfImage> getImageList() {
        return imageList;
    }

    public void addToImageList(final PlaatsBeschrijfImage image){
        if(!any(imageList, new Predicate<PlaatsBeschrijfImage>() {
                    @Override public boolean apply(final PlaatsBeschrijfImage input) {
                        return input.getImageURL().equals(image.getImageURL());
                    }
                })) {
            imageList.add(image);
        }
    }

    public void setImageList(final List<PlaatsBeschrijfImage> imageList) {
        this.imageList = imageList;
    }

    public boolean hasImages(){
        return !isEmpty(imageList);
    }

    public void setLocation(final String location) {
        this.location = location;
    }
}
