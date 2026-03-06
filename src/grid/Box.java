package grid;
import core.Player;
import core.Tile;

public class Box extends Tile {
    
    private final Line top;
    private final Line bottom;
    private final Line left;
    private final Line right;

    private boolean filled;
    private Player filledBy;

    private int sidesDrawn;

    public Box(int row, int col, Line top, Line bottom, Line left, Line right) {
        super(row,col);
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.filled = false;
        this.filledBy = null;
        this.sidesDrawn = 0;
    }

    public Line getTop() {
        return top;
    }

    public Line getBottom() {
        return bottom;
    }

    public Line getLeft() {
        return left;
    }

    public Line getRight() {
        return right;
    }

    public boolean isFilled() {
        return filled;
    }

    public int updateIfFilled(Player lastPlayer) {
        if (filled) {
            return 0; // already scored
        }
        int prevSidesDrawn = sidesDrawn;

        int count = 0;
        if (top.isDrawn()) count++;
        if (bottom.isDrawn()) count++;
        if (left.isDrawn()) count++;
        if (right.isDrawn()) count++;
        sidesDrawn = count;

        if (prevSidesDrawn == 3 && sidesDrawn == 4) {
            filled = true;
            filledBy = lastPlayer;
            return 1;
        }
        return 0;
    }

    public void fill(Player player) {
        filled = true;
        filledBy = player; 
    }

    public void free() {
        filled = false;
        filledBy = null;
    }

    public Player isFilledBy() {
        return filledBy;
    }

    public int getSidesDrawn() {
        return sidesDrawn;
    }

}
