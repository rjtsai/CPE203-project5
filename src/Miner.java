import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Miner extends AnimationEntity implements Moveable{
    protected int resourceCount;
    protected int resourceLimit;

    public Miner(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod, int repeatCount) {
        super(id, position, images, actionPeriod, animationPeriod, 0);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public int getResourceCount(){ return this.resourceCount; }
    public int getResourceLimit(){ return this.resourceLimit; }

    public boolean transform(WorldModel world,
                             EventScheduler scheduler, ImageStore imageStore) {
        if (this.getClass().equals(MinerNotFull.class)) {
            if (getResourceCount() >= getResourceLimit()) {
                MinerFull miner = CreateFactory.createMinerFull(getId(), getPosition(), getResourceLimit(),
                        getResourceCount(), getActionPeriod(), getAnimationPeriod(),
                        getImages());
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);
                world.addEntity(miner);
                miner.scheduleActions(scheduler, world, imageStore);
                return true;
            }
            return false;
        }
        else {
            ActionEntity miner = CreateFactory.createMinerNotFull(getId(), getPosition(), getResourceLimit(),
                     getActionPeriod(), getAnimationPeriod(),
                    getImages());
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);
            return false;
        }
    }

    public Point nextPosition(WorldModel world, Point destPos)
    {
        AStarPathingStrategy pathing = new AStarPathingStrategy();

        List<Point> pathPoints = pathing.computePath(this.getPosition(), destPos,
                (p -> world.withinBounds(p) && !(world.isOccupied(p))),
                ((p1, p2) -> p1.adjacent(p2)),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (pathPoints.isEmpty()){
            return this.getPosition();
        }

        // System.out.println("MOVING ENTITY FIRST STEP: " + pathPoints.get(0));
        return pathPoints.get(0);
    }
}