import java.awt.Color;
import java.util.Random;

/**
 * ColourGenerator.java
 * Helper class. Creates the two colours for each round and decides
 * how big the grid should be. Keeping this logic here keeps
 * GamePanel shorter and easier to read.
 */
public class ColourGenerator {

    private Random random = new Random();
    private Color baseColour;   // colour used by most boxes
    private Color oddColour;    // colour of the one different box

    /**
     * Generates a new pair of colours for the given level.
     * The higher the level, the smaller the difference between the colours.
     */
    public void generate(int level) {
        // Pick random red, green, blue values between 60 and 195.
        // This range keeps the colours away from pure black/white and
        // guarantees that adding or subtracting "diff" never goes
        // outside the valid 0-255 range.
        int r = 60 + random.nextInt(136);
        int g = 60 + random.nextInt(136);
        int b = 60 + random.nextInt(136);

        // Difference shrinks as level goes up:
        // level 1 = 45, level 2 = 38, level 3 = 31 ... minimum = 6
        int diff = Math.max(6, 45 - (level - 1) * 7);

        // Randomly make the odd box lighter or darker than the others
        int sign = random.nextBoolean() ? 1 : -1;

        baseColour = new Color(r, g, b);
        oddColour = new Color(r + sign * diff, g + sign * diff, b + sign * diff);
    }

    /**
     * Level 1 = 3x3, Level 2 = 4x4, Level 3 = 5x5, Level 4 and above = 6x6.
     */
    public static int getGridSize(int level) {
        return Math.min(level + 2, 6);
    }

    public Color getBaseColour() {
        return baseColour;
    }

    public Color getOddColour() {
        return oddColour;
    }
}