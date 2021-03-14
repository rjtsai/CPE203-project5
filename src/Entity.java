import java.util.List;
import java.util.Optional;

import processing.core.PImage;
public abstract class Entity {
    protected String id;
    protected Point position;
    protected int imageIndex;
    protected List<PImage> images;

    public Entity(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public String getId() {
        return this.id;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point point){ this.position = point; }

    public int getImageIndex() {
        return this.imageIndex;
    }

    public List<PImage> getImages() {
        return this.images;
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public PImage getCurrentImage(Object entity) {
        if (entity instanceof Background) {
            return ((Background) entity).getImages()
                    .get(((Background) entity).getImageIndex());
        }
        else if (entity instanceof Entity) {
            return (this.getImages().get(this.getImageIndex()));
        }
        else {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
    }
}
