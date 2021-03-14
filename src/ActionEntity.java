import processing.core.PImage;
import java.util.List;

public abstract class ActionEntity extends Entity {
    protected int actionPeriod;

    public ActionEntity(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() { return this.actionPeriod; }

    public void scheduleActions (EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                CreateAction.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    protected abstract void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}