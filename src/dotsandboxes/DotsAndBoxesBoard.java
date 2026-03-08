package dotsandboxes;

import java.util.List;
import java.util.ArrayList;
import core.Player;
import core.Tile;
import grid.GridBoard;
import grid.Box;
import grid.Direction;
import grid.Dot;
import grid.Line;

public class DotsAndBoxesBoard extends GridBoard {
    public DotsAndBoxesBoard(int h, int w) {
        super(h,w);
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
