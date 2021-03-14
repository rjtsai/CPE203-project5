import java.util.*;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PApplet;

public final class Functions
{
    public static final Random rand = new Random();

    /*

    public static final int COLOR_MASK = 0xffffff;
    public static final int KEYED_IMAGE_MIN = 5;
    private static final int KEYED_RED_IDX = 2;
    private static final int KEYED_GREEN_IDX = 3;
    private static final int KEYED_BLUE_IDX = 4;

    public static final int PROPERTY_KEY = 0;

    public static final String BGND_KEY = "background";

     */



    public static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case VirtualWorld.FAST_FLAG:
                    VirtualWorld.timeScale = Math.min(VirtualWorld.FAST_SCALE, VirtualWorld.timeScale);
                    break;
                case VirtualWorld.FASTER_FLAG:
                    VirtualWorld.timeScale = Math.min(VirtualWorld.FASTER_SCALE, VirtualWorld.timeScale);
                    break;
                case VirtualWorld.FASTEST_FLAG:
                    VirtualWorld.timeScale = Math.min(VirtualWorld.FASTEST_SCALE, VirtualWorld.timeScale);
                    break;
            }
        }
    }

    public static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }


}
