import processing.core.PImage;

import java.util.List;

public class Lightning extends AnimationEntity{
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Lightning(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod, QUAKE_ANIMATION_REPEAT_COUNT);
    }

    public void execute(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


}