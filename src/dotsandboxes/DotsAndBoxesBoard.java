package dotsandboxes;

import java.util.List;
import java.util.ArrayList;
import core.Player;
import core.Tile;
import boardtwo.BoardTwo;

public class DotsAndBoxesBoard extends BoardTwo {

    int height;
    int width;
    private final Tile[][] board;
    private Dot[][] grid;
    private Line[][] horizontalLines;
    private Line[][] verticalLines;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    public DotsAndBoxesBoard(int h, int w) {
        this.height = h;
        this.width = w;
        this.board =  new Box[height][width];
        
        initialize();
    }

    @Override
    public void initialize() {
        initalizeGrid();
        initializeLines();
        initializeBoxes();
    }

    private void initalizeGrid() {
        this.grid = new Dot[height+1][width+1];

        for (int r = 0; r <= height; r++) {
            for (int c = 0; c <= width; c++) {
                grid[r][c] = new Dot(r, c);
            }
        }
    }

    private void initializeLines() {
        
        this.horizontalLines = new Line[height+1][width];

        for (int r = 0; r < horizontalLines.length; r++) {
            for (int c = 0; c < horizontalLines[0].length; c++) {
                this.horizontalLines[r][c] = new Line(grid[r][c],grid[r][c+1]);
            }
        }

        this.verticalLines = new Line[height][width+1];

        for (int r = 0; r < verticalLines.length; r++) {
            for (int c = 0; c < verticalLines[0].length; c++) {
                this.verticalLines[r][c] = new Line(grid[r][c],grid[r+1][c]);
            }
        }
    }

    private void initializeBoxes() {
        for (int r = 0; r < height; r++) { 
            for (int c = 0; c < width; c++) {
                Line top = getLine(r, c, Direction.HORIZONTAL);
                Line left = getLine(r, c, Direction.VERTICAL);
                Line bottom = getLine(r + 1, c, Direction.HORIZONTAL);
                Line right = getLine(r, c + 1, Direction.VERTICAL);
                this.board[r][c] = new Box(r,c,top,left,bottom,right);
            }
        }     
    }

    private Line getLine(int row, int col, Direction direction) { 
        if (direction ==  Direction.HORIZONTAL) {
            return horizontalLines[row][col];
        }
        if (direction == Direction.VERTICAL) {
            return verticalLines[row][col];
        }

        return null;
    }

    private Line getLineBetween(Dot a, Dot b) {
        int r1 = a.getRow();
        int c1 = a.getCol();
        int r2 = b.getRow();
        int c2 = b.getCol();

        if (r1 == r2) { // horizontal
            int col = Math.min(c1, c2);
            return horizontalLines[r1][col];
        } else { // vertical
            int row = Math.min(r1, r2);
            return verticalLines[row][c1];
        }
    }
    
    public Dot getDot(String str) {
        int row = str.charAt(0) - 'a';
        int col = str.charAt(1)  - '1';
        return grid[row][col];
    }

    @Override
    public void print() { 
        printHorizontalKeys();
        for (int r = 0; r < height+1; r++) {
            printRowHorizontal(r); 
            if (r < height) {                        // only print vertical lines if not last dot row
                printRowVertical(r);               // prints vertical lines below
            }
        }
    }

    private void printRowHorizontal(int r) {
        printVericalKey(r);
        for (int c = 0; c < width; c++) {
            Line l = getLine(r,c,Direction.HORIZONTAL);
            System.out.print("*");
            if (!l.isDrawn()){
                System.out.print("   ");
            } else {
                if (l.getDrawnBy().getNumber() == 1) {
                    System.out.print(ANSI_PURPLE + "---" + ANSI_RESET);
                } else {
                    System.out.printf(ANSI_BLUE + "---" + ANSI_RESET);
                }
                
            } 
        }
        System.out.printf("*\n");
    }

    private void printRowVertical(int row) {
        System.out.print("   ");   
        for (int c = 0; c <= width; c++) {      // iterate over columns of vertical lines
            Line l = verticalLines[row][c];

            if (l.isDrawn()) {
                if (l.getDrawnBy().getNumber() == 1) {
                    System.out.print(ANSI_PURPLE +"|" + ANSI_RESET);  
                      // drawn vertical line
                } else {
                     System.out.print(ANSI_BLUE +"|" + ANSI_RESET);        
                }
            } else {
                System.out.print(" ");           // empty space if not drawn
            }

            // add spacing between vertical lines (matches horizontal spacing)
            if (c < width) {
                if (((Box) board[row][c]).isFilled()) {
                    if (((Box) board[row][c]).isFilledBy().getNumber() == 1) {
                        System.out.print(ANSI_PURPLE_BACKGROUND + " X " + ANSI_RESET); 
                    } else {
                        System.out.print(ANSI_BLUE_BACKGROUND + " X " + ANSI_RESET);
                    }
                    
                } else {
                    System.out.print("   ");         // space between columns
                }
                
            }
        }
        System.out.println();
    }

    private void printHorizontalKeys() {
        System.out.printf("   ");
        for (int c = 0; c <= width; c++) {
            System.out.printf("%d   ", c+1);
        }
        System.out.println();
    }

    private void printVericalKey(int r) {
        char letter = (char) ('a' + r);
        System.out.printf("%s  ", letter);
    }


    
    @Override
    public boolean solved() {
        for (int r = 0; r < height; r++) { 
            for (int c = 0; c < width; c++) {
                if (!((Box) board[r][c]).isFilled()) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean validatePoint(String str) {
        if (str.length() != 2) {
            return false;
        }
        char rowChar = str.charAt(0);
        char colChar = str.charAt(1);

        if (!Character.isLowerCase(rowChar)) {
            return false;
        }

        if (!Character.isDigit(colChar)) {
            return false;
        }

        int row = rowChar - 'a';
        int col = colChar - '1';
        if (row >= 0 && row <= height && col >= 0 && col <= width) {
            return true;
        }
        return false;
    }

    public boolean validateMove(Dot start, Dot end) {
        if ((Math.abs(start.getRow() - end.getRow()) + Math.abs(start.getCol() - end.getCol())) == 1) {
            Line line = getLineBetween(start,end);
            if (line.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public int draw(Dot dot1, Dot dot2, Player player) {
        Line line = getLineBetween(dot1,dot2);
        line.draw(player);
    
        return updateBoxes(line, player);
    }

    private int updateBoxes(Line line, Player lastPlayer) {
        int r1 = line.getStart().getRow();
        int c1 = line.getStart().getCol();
        int r2 = line.getEnd().getRow();
        
        int filled = 0;
        if (r1 == r2) { // horizontal
            if (r1 - 1 >= 0) {
                filled += ((Box) board[r1-1][c1]).updateIfFilled(lastPlayer);
            }
            if (r1 < height) {
                filled += ((Box) board[r1][c1]).updateIfFilled(lastPlayer);
            }
        } else { // vertical
            if (c1 - 1 >= 0) {
                filled += ((Box) board[r1][c1-1]).updateIfFilled(lastPlayer);
            }     
            if (c1 < width) {
                filled += ((Box) board[r1][c1]).updateIfFilled(lastPlayer);
            }
        }
        return filled;
    }
    
    public List<Line> getAvailableLines() {
        List<Line> availableLines = new ArrayList<>();
        
        // horizontal lines
        for (int r = 0; r < horizontalLines.length; r++) {
            for (int c = 0; c < horizontalLines[0].length; c++) {
                if (!horizontalLines[r][c].isDrawn()) {
                    availableLines.add(horizontalLines[r][c]);
                }
            }
        }
        // vertical lines
        for (int r = 0; r < verticalLines.length; r++) {
            for (int c = 0; c < verticalLines[0].length; c++) {
                if (!verticalLines[r][c].isDrawn()) {
                    availableLines.add(verticalLines[r][c]);
                }
            }
        }

        return availableLines;
    }

    public Line getMissingLine(Box box) {

        if (!box.getTop().isDrawn()) {
            return box.getTop();
        }

        if (!box.getLeft().isDrawn()) {
            return box.getLeft();
        }

        if (!box.getBottom().isDrawn()) {
            return box.getBottom();
        }

        if (!box.getRight().isDrawn()) {
            return box.getRight();
        }

        return null; // all sides drawn
    }

    public Line tryAlmostCompleteBox() {
        
        for (int r = 0; r < height; r++) {        
            for (int c = 0; c < width; c++) {                    
                if (((Box) board[r][c]).getSidesDrawn() == 3) {
                    Line missing = getMissingLine(((Box) board[r][c]));
                    if (missing != null && !missing.isDrawn()) {
                        return missing;
                        
                    }
                }
            }
        }
        return null;
    }
}
