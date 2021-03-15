import processing.core.PImage;

import java.util.List;

public class CreateFactory {

    public static BlackSmith createBlacksmith(
            String id, Point pos, List<PImage> images)
    {
        return new BlackSmith(id, pos, images);
    }

    public static MinerFull createMinerFull(
            String id, Point pos,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new MinerFull(id, pos, images,
                resourceLimit, resourceCount, actionPeriod,
                animationPeriod);
    }

    public static MinerNotFull createMinerNotFull(
            String id, Point pos,
            int resourceLimit,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new MinerNotFull(id, pos, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public static Obstacle createObstacle(
            String id, Point pos, List<PImage> images)
    {
        return new Obstacle(id, pos, images);
    }

    public static Ore createOre(
            String id, Point pos, int actionPeriod, List<PImage> images)
    {
        return new Ore(id, pos, images,
                actionPeriod);
    }

    public static OreBlob createOreBlob(
            String id, Point pos,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new OreBlob(id, pos, images, 0, 0,
                actionPeriod, animationPeriod);
    }

    public static Charizard createCharizard(
            String id, Point pos,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Charizard(id, pos, images, 0, 0,
                actionPeriod, animationPeriod);
    }

    public static Quake createQuake(Point pos,
            List<PImage> images)
    {
        return new Quake(Parser.QUAKE_ID, pos, images,
                Parser.QUAKE_ACTION_PERIOD, Parser.QUAKE_ANIMATION_PERIOD);
    }

    public static Vein createVein(
            String id, Point pos, int actionPeriod, List<PImage> images)
    {
        return new Vein(id, pos, images,
                actionPeriod, 0);
    }



}