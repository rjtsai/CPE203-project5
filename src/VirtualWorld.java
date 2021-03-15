import processing.core.*;

import java.util.LinkedList;

public final class VirtualWorld extends PApplet
{
    public static final int TIMER_ACTION_PERIOD = 100;

    public static final int VIEW_WIDTH = 640;
    public static final int VIEW_HEIGHT = 480;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int WORLD_WIDTH_SCALE = 2;
    public static final int WORLD_HEIGHT_SCALE = 2;

    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    public static final String IMAGE_LIST_FILE_NAME = "imagelist";
    public static final String DEFAULT_IMAGE_NAME = "background_default";
    public static final int DEFAULT_IMAGE_COLOR = 0x808080;

    public static final String LOAD_FILE_NAME = "world.sav";

    public static final String FAST_FLAG = "-fast";
    public static final String FASTER_FLAG = "-faster";
    public static final String FASTEST_FLAG = "-fastest";
    public static final double FAST_SCALE = 0.5;
    public static final double FASTER_SCALE = 0.25;
    public static final double FASTEST_SCALE = 0.10;

    public static double timeScale = 1.0;

    public ImageStore imageStore;
    public WorldModel world;
    public WorldView view;
    public EventScheduler scheduler;
    public Viewport viewPort;

    public long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                ImageStore.createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    imageStore.createDefaultBackground());
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        ImageStore.loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        world.loadWorld(LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    public void mousePressed()
    {
        Point location = mouseToPoint(mouseX, mouseY);
        LinkedList<Point> box = new LinkedList<>();
        box.add(new Point(location.getX() +1, location.getY()));
        box.add(new Point(location.getX() +1, location.getY()+1));
        box.add(new Point(location.getX() +1, location.getY()-1));
        box.add(new Point(location.getX() -1, location.getY()));
        box.add(new Point(location.getX() -1, location.getY()+1));
        box.add(new Point(location.getX() -1, location.getY()-1));
        box.add(new Point(location.getX(), location.getY()+1));
        box.add(new Point(location.getX(), location.getY()-1));
        Charizard newCharz = CreateFactory.createCharizard("charizard", location, 0, 0, imageStore.getImageList("charizard"));
                //new Charizard("charizard", location,imageStore.getImageList("charizard"), 0, 0, 40, 100);
        if (!(world.isOccupied(location))) {
            world.addEntity(newCharz);
            newCharz.scheduleActions(scheduler, world, imageStore);
        }


        for (int i = location.getX() - 3; i < location.getX() + 4; i++) {
            for (int j = location.getY() - 3; j < location.getY() + 4; j++) {
                if (world.withinBounds(new Point(i, j))) {
                    world.setBackgroundCell(new Point(i, j), new Background("dirt", imageStore.getImageList("dirt")));
                    Vein newVein = new Vein("vein", new Point(i, j), imageStore.getImageList("vein"), 0, 0);
                    Vein newVein1 = new Vein("vein", new Point(i, j), imageStore.getImageList("vein"), 0, 10000000);
                    if (box.contains(new Point(i, j))) {
                        world.addEntity(newVein1);
                        //newVein1.scheduleActions(scheduler, world, imageStore);
                    } //else if (i == location.getX() -3 || )
                }
            }
        }
        BlackSmith newBS = new BlackSmith("blacksmith", new Point(location.getX() + 2, location.getY() - 2), imageStore.getImageList("blacksmith"));
        world.addEntity(newBS);
        /*
        Optional<Entity> warmMiner = world.findNearest(location, MinerEntity.class);
        if (warmMiner.isPresent()) {
            MinerEntity realWarmMiner = ((MinerEntity)(warmMiner.get()));
            MovableEntity miner = new ColdMiner("frozenminer", realWarmMiner.getPosition(), imageStore.getImageList("frozenminer"),realWarmMiner.getActionPeriod() * 20, realWarmMiner.getAnimationPeriod() * 10, realWarmMiner);
            world.removeEntity(realWarmMiner);
            scheduler.unscheduleAllEvents(realWarmMiner);
            scheduler.scheduleActions(miner, world, imageStore);

            world.addEntity(miner);
        }

         */

    }

    private Point mouseToPoint(int x, int y)
    {
        return new Point(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }




    public void scheduleActions(WorldModel world,
            EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.entities) {
            if (entity instanceof ActionEntity) {
                ((ActionEntity)entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    public static void main(String[] args) {
        Functions.parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
