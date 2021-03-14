import java.util.List;

import processing.core.PImage;

public final class Background
{
    public String id;
    public List<PImage> images;
    public int imageIndex;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public String getId() { return id; }

    public List<PImage> getImages() { return images;}

    public int getImageIndex() { return imageIndex; }

}
