public class ActivityAction implements Action{
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;

    public ActivityAction(Entity entity, WorldModel world, ImageStore imageStore){
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        ((ActionEntity)this.entity).execute(this.world, this.imageStore, scheduler);
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    public WorldModel getWorld(){ return this.world; }

    public ImageStore getImageStore() {
        return imageStore;
    }
}