public class CreateAction {
    public static Action createAnimationAction (AnimationEntity entity, int repeatCount){
        return new AnimationAction(entity, repeatCount);
    }

    public static Action createActivityAction (Entity entity, WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction(entity, world, imageStore);
    }
}