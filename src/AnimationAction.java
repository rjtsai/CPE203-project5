public class AnimationAction implements Action{
    private Entity entity;
    private int repeatCount;

    public AnimationAction(AnimationEntity entity, int repeatCount){
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        this.entity.nextImage();
        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    CreateAction.createAnimationAction(((AnimationEntity)this.entity), Math.max(this.repeatCount - 1, 0)),
                    ((AnimationEntity) this.entity).getAnimationPeriod());
        }
    }

    public Entity getEntity() {
        return this.entity;
    }
    public int getRepeatCount(){ return this.repeatCount; }
}
