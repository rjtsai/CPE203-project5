import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class WorldModel
{
    public int numRows;
    public int numCols;
    public Background background[][];
    public Entity occupancy[][];
    public Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public void loadWorld(String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean processLine(
            String line, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[Parser.PROPERTY_KEY]) {
                case Parser.BGND_KEY:
                    return Parser.parseBackground(properties, this, imageStore);
                case Parser.MINER_KEY:
                    return Parser.parseMiner(properties, this, imageStore);
                case Parser.OBSTACLE_KEY:
                    return Parser.parseObstacle(properties, this, imageStore);
                case Parser.ORE_KEY:
                    return Parser.parseOre(properties, this, imageStore);
                case Parser.SMITH_KEY:
                    return Parser.parseSmith(properties, this, imageStore);
                case Parser.VEIN_KEY:
                    return Parser.parseVein(properties, this, imageStore);
            }
        }

        return false;
    }

    private void load(
            Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                                                     lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                      e.getMessage()));
            }
            lineNumber++;
        }
    }

    public Optional<Entity> findNearest(
            Point pos, Class c)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.entities) {
            if (entity.getClass() == c) {
                ofType.add(entity);
            }
        }

        return pos.nearestEntity(ofType);
    }

    private void setOccupancyCell(
            Point pos, Entity entity)
    {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public Optional<PImage> getBackgroundImage(
            Point pos)
    {
        if (withinBounds(pos)) {
            return Optional.of(ImageStore.getCurrentImage(this.getBackgroundCell(pos)));
        }
        else {
            return Optional.empty();
        }
    }

    private void removeEntityAt(Point pos) {
        if (withinBounds(pos) && this.getOccupancyCell(pos) != null) {
            Entity entity = this.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public void removeEntity(Entity entity) {
        removeEntityAt(entity.getPosition());
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && this.getOccupancyCell(pos) != null;
    }

    public void tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity(entity);
    }

    private Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    public void setBackground(
            Point pos, Background background) {
        if (withinBounds(pos)) {
            this.setBackgroundCell(pos, background);
        }
    }

    private void setBackgroundCell(
            Point pos, Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

    private Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }
    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0
                && pos.x < this.numCols;
    }

    public Optional<Point> findOpenAround(Point pos) {
        for (int dy = -Vein.ORE_REACH; dy <= Vein.ORE_REACH; dy++) {
            for (int dx = -Vein.ORE_REACH; dx <= Vein.ORE_REACH; dx++) {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (withinBounds(newPt) && !isOccupied(newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    /*
           Assumes that there is no entity currently occupying the
           intended destination cell.
        */
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }



}
