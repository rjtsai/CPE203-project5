import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Pikachu extends AnimationEntity implements Moveable{
    private static final String QUAKE_KEY = "quake";
    private static final Random rand = new Random();

    public Pikachu(String id, Point position,
                     List<PImage> images, int resourceLimit, int resourceCount,
                     int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod, 0);
    }

    public void execute(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> OreblobTgt =
                world.findNearest(this.position, OreBlob.class);
        long nextPeriod = this.actionPeriod;

        if (OreblobTgt.isPresent()) {
            Point tgtPos = OreblobTgt.get().getPosition();

            if (this.moveTo(world, OreblobTgt.get(), scheduler)) {
                Lightning lightning = CreateFactory.createLightning(tgtPos,
                        imageStore.getImageList("lightning"));

                world.addEntity(lightning);
                nextPeriod += this.actionPeriod;
                lightning.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                CreateAction.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (getPosition().adjacent(target.getPosition())) {
            //world.removeEntity(target);
            Point newP = target.getPosition().teleport();
            while (!world.withinBounds(newP)) {
                newP = target.getPosition().teleport();
                System.out.println("Out of bounds: " + newP.toString());
            }
            world.moveEntity(target, newP);
            //scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public Point nextPosition(
            WorldModel world, Point destPos)
    {
        Point currPos = this.position;
        AStarPathingStrategy pathing = new AStarPathingStrategy();

        List<Point> pathPoints = pathing.computePath(this.getPosition(), destPos,
                (p -> world.withinBounds(p) && !(world.isOccupied(p))),
                ((p1, p2) -> p1.adjacent(p2)),
                PathingStrategy.CARDINAL_NEIGHBORS);


        if (pathPoints.isEmpty()){
            return this.getPosition();
        }


        return pathPoints.get(0);
    }


}