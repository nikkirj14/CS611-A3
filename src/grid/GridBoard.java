package grid;

import core.Board;
import core.Tile;

public abstract class GridBoard implements Board {
    public int height;
    public int width;
    public final Tile[][] board;
    public Dot[][] grid;
    public Line[][] horizontalLines;
    public Line[][] verticalLines;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    public GridBoard(int h, int w) {
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

    public Line getLine(int row, int col, Direction direction) { 
        if (direction ==  Direction.HORIZONTAL) {
            return horizontalLines[row][col];
        }
        if (direction == Direction.VERTICAL) {
            return verticalLines[row][col];
        }

        return null;
    }

    public Line getLineBetween(Dot a, Dot b) {
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

}
