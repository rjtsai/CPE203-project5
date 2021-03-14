import processing.core.PImage;
import java.util.List;

public abstract class AnimationEntity extends ActionEntity{
    protected int repeatCount;
    protected int animationPeriod;

    public AnimationEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                           int repeatCount) {
        super(id, position, images, actionPeriod);
        this.repeatCount = repeatCount;
        this.animationPeriod = animationPeriod;
    }

    public int getRepeatCount(){ return this.repeatCount; }
    public int getAnimationPeriod(){ return this.animationPeriod; }

    public void scheduleActions (EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                CreateAction.createActivityAction(this, world, imageStore),
                this.getAnimationPeriod());
        scheduler.scheduleEvent(this,
                CreateAction.createAnimationAction(this, repeatCount), this.getAnimationPeriod());
    }
}