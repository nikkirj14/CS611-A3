package slidingpuzzle;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import core.Board;
import core.Tile;


// This class manages the sliding puzzle state, the board layout and game rules. 
public class SlidingPuzzleBoard implements Board {

    int height;
    int width;
    int emptycol;
    int emptyrow;
    private final Tile[][] board; // board for structure
    private final Map<Integer, Block> map; // hashmap for lookup

    // Puzzle constructor
    public SlidingPuzzleBoard(int h, int w) { 
        this.height = h;
        this.width = w;
        this.board = new Block[height][width];
        this.map = new HashMap<>();
        this.emptycol = width-1;
        this.emptyrow = height-1;
    }
    
    // Helper function to repeat chars
    private String repeat(char c, int times) {
        StringBuilder str = new StringBuilder(times);
        for (int i = 0; i < times; i++) {
            str.append(c);
        }
        return str.toString();
    }

    @Override
    // Prints the puzzle
    public void print() {
        int max = height * width - 1;
        int maxWidth = String.valueOf(max).length()+2; //maxwidth
        
        // Dynamically define puzzle border
        StringBuilder border = new StringBuilder("+");
        for (int i = 0; i < width; i++) {
            border.append(repeat('-', maxWidth + 2)); 
            border.append("+");
        }
        String borderString = border.toString();

        // Print all rows
        for (int i = 0; i < height; i++) {
            System.out.println(borderString);
            printRow((board[i]), maxWidth);
        }
        System.out.println(borderString);
    }

    // Prints a single row
    private void printRow(Tile[] row, int maxWidth) {
        System.out.print("|");
        for (Tile t : row) {
            Block b = (Block) t;
            
            if ( b.isEmpty()) {
                System.out.print(repeat(' ', maxWidth + 2)); // space for empty
            } else {
                String value = String.valueOf(b.getValue());
                int padding = maxWidth - value.length(); // extra padding if smaller than max digits
                int paddingL = padding/2;
                int paddingR = padding - paddingL;
                System.out.print(" " + repeat(' ', paddingL) + value + repeat(' ', paddingR) + " ");
            }
            System.out.print("|");
        }
        System.out.println();
    }

    @Override
    public void initialize() {
        int max = width * height - 1;
        int counter = 1;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                Integer val;
                if (counter <= max) {
                    val = counter; 
                } else {
                    val = null; 
                }
                Block b = new Block(r, c, val);
                board[r][c] = b;
                map.put(val, b);
                counter++;
            }
        }
        emptyrow = height - 1;
        emptycol = width - 1;

        // Doing 3-10 random legal sliding moves to shuffle
        int numMoves = 5 + (int)(Math.random() * 10); 
        for (int i = 0; i < numMoves; i++) {
            List<Block> neighbors = getMovable();
            if (!neighbors.isEmpty()) {
                // Pick a random option from neighbors to slide
                Block selected = neighbors.get((int)(Math.random() * neighbors.size()));
                slide(selected.getValue());
            }
        }
    }

    private List<Block> getMovable() {
        List<Block> neighbors = new ArrayList<>();

        if (emptyrow > 0) {
            neighbors.add(((Block) board[emptyrow - 1][emptycol]));
        }
       
        if (emptyrow < height - 1) {
            neighbors.add(((Block) board[emptyrow + 1][emptycol]));
        }
        
        if (emptycol > 0) {
            neighbors.add(((Block) board[emptyrow][emptycol - 1]));
        }
       
        if (emptycol < width - 1) {
            neighbors.add(((Block) board[emptyrow][emptycol + 1]));
        }
        return neighbors;
    }

    @Override
    // Checks if puzzle has been solved
    public boolean solved() {
        int counter=1;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Block b = (Block) board[i][j];

                if (b.value == null) {
                    return false;
                }
                if (b.value != counter){
                    return false;
                }
                counter++;
                // reaching the end of the board means solved
                if (counter == width*height) {
                    return true;
                }
            }
        }
        return true;
    }

    // Checks if selected block can be moved
    public boolean validateMove(int input) {

        Block b = (Block) map.get(input);

        if (b == null) {
            return false;
        }
               // if difference with rows OR columns of block and empty block is 1 then it is a valid move
        if ((Math.abs(b.getRow() - emptyrow) + Math.abs(b.getCol() - emptycol)) == 1) {
            return true;
        }

        return false;
    }
    
    // Slides block to new position
    public void slide(int input) {
        Block b = (Block) map.get(input);
        Block empty = (Block) board[emptyrow][emptycol];

        int oldRow = b.getRow();
        int oldCol = b.getCol();

        // update board
        board[emptyrow][emptycol] = b;
        board[b.getRow()][b.getCol()] = empty;


        // switching tile and empty tile positions
        b.setRow(emptyrow);
        b.setCol(emptycol);

        empty.setCol(oldCol);
        empty.setRow(oldRow);

        emptyrow = oldRow;
        emptycol = oldCol;
    }

}