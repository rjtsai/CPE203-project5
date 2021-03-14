import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miner{

    public MinerNotFull(String id, Point position, List<PImage> images,
                     int resourceLimit, int resourceCount, int actionPeriod,
                     int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, 0);
    }
    public void setPosition(Point p){ this.position = p;}
    public List<PImage> getImages() { return this.images; }

    public int getImageIndex() { return this.imageIndex; }

    public Point getPosition() { return this.position; }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;  // placeholder
    }


    public void execute(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> notFullTarget =
                world.findNearest(this.position, Ore.class);

        if (!notFullTarget.isPresent() || !moveTo(world,
                notFullTarget.get(),
                scheduler)
                || !this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    CreateAction.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }



    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.getPosition())) {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }



}