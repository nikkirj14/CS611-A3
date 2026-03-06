package quorridor;

import java.util.function.DoubleToIntFunction;

import core.Player;
import grid.Box;
import grid.Direction;
import grid.Dot;
import grid.GridBoard;
import grid.Line;

public class QuorridorBoard extends GridBoard {

    protected int maxBorders;
    protected Box[] position;

    protected int borders1;
    protected int borders2;

    QuorridorBoard(int h, int w, Player[] players) {
        super(h,w);
        maxBorders = Math.max(h,w) - 1;
        position = new Box[2];
        position[0] = (Box) board[h-1][w/2];
        ((Box) board[h-1][w/2]).fill(players[0]);

        if (w % 2 != 0) {
            position[1] = (Box) board[0][w/2];
            ((Box) board[0][w/2]).fill(players[1]);
        } else {    
            position[1] = (Box) board[0][w/2-1]; 
            ((Box) board[0][w/2-1]).fill(players[1]);   
        }     
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
                if (((Box) board[row][c]) == position[0]) {
                    System.out.print(ANSI_PURPLE_BACKGROUND + " X " + ANSI_RESET); 
                } else if (((Box) board[row][c]) == position[1]) {
                    System.out.print(ANSI_BLUE_BACKGROUND + " X " + ANSI_RESET); 
                }  else {
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

    public int validateBorder(Dot a , Dot b) {
        int rowDiff = Math.abs(a.getRow() - b.getRow());
        int colDiff = Math.abs(a.getCol() - b.getCol());

        if ((rowDiff == 2 && colDiff == 0) || (rowDiff == 0 && colDiff == 2)) {
            Dot mid = getMidpoint(a,b);
            Line line1 = getLineBetween(a,mid);
            Line line2 = getLineBetween(mid,b);
            if (line1.isEmpty() && line2.isEmpty()) {

                if (willCompletelyBlockOpponent(a,mid,b)) {
                    return 2;
                }
                return 1;
            }
        }

        return 0;
    }

    public int validateMove(String direction, int player) {
    
        Box currPos = position[player];
        int r = currPos.getRow();
        int c = currPos.getCol();

        int newRow = r;
        int newCol = c;
        Line border = null;

        switch (direction) {
            case "R":
                newCol = c + 1;
                if (newCol >= width) {
                    return 0;
                }
                border = verticalLines[r][c + 1]; 
                break;
            case "L":
                newCol = c - 1;
                if (newCol < 0) {
                    return 0;
                }
                border = verticalLines[r][c]; 
                break;
            case "U":
                newRow = r - 1;
                if (newRow < 0) {
                    return 0;
                }
                border = horizontalLines[r][c]; 
                break;
            case "D":
                newRow = r + 1;
                if (newRow >= height) {
                    return 0;
                }
                border = horizontalLines[r + 1][c]; 
                break;
            default:
                return 0;
        }

        if (border.isDrawn()) return 1;          // blocked by border
        if (((Box) board[newRow][newCol]).isFilled()) return 2; // tile occupied
        return 3; // valid
    }

    public boolean willCompletelyBlockOpponent(Dot a, Dot b, Dot c) {
        return false; //TODO
    }

    public void drawBorder(Dot a, Dot b, Player player) {
        Dot mid = getMidpoint(a, b); // for borders that span 2 units

        Line line1 = getLineBetween(a, mid);
        line1.draw(player);

        Line line2 = getLineBetween(mid, b);
        line2.draw(player);


        // TODO: 
        // check if less than max borders
        // increment borders drawn by player
    }

    private Dot getMidpoint(Dot a , Dot b) {
        int r = (a.getRow() + b.getRow()) / 2;
        int c = (a.getCol() + b.getCol()) / 2;

        return grid[r][c];
    }

    public void move(int num, Player player, String direction) {
        int currRow = position[num].getRow();
        int currCol = position[num].getCol();
        ((Box) board[currRow][currCol]).free();

        int row = currRow;
        int col = currCol;
        // checking if within bounds of board and then if there is border blocking
        if (direction.equals("R")) {
            col = currCol + 1;
            
        } else if (direction.equals("L")) {
            col = currCol - 1;
            
        } else if (direction.equals("U")) {
            row = currRow - 1;

        } else if (direction.equals("D")) {
            row = currRow + 1;
        }
        
        position[num] = (Box) board[row][col];
        ((Box) board[row][col]).fill(player);
    }

    @Override
    public boolean solved() {
        return false; //TODO
    }


}
