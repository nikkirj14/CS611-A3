package slidingpuzzle;
import core.Tile;

// This class models single units that make up the puzzle.
public class Block extends Tile {

    protected Integer value;

    Block(int row, int col, Integer val) {
        super(row,col);
        this.value = val;
    }

    // Extracts value from Block
    public Integer getValue() {
        return value;
    }

    // Checks if Block contains null value
    public boolean isEmpty() {
        return value == null;
    }
}
