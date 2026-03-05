package quorridor;

import core.Player;
import boardtwo.BoardTwo;
import dotsandboxes.Dot;
import dotsandboxes.DotsAndBoxesBoard;
import dotsandboxes.Line;
import dotsandboxes.Box;
import dotsandboxes.Direction;

public class QuorridorBoard extends BoardTwo {

    int maxBorders;
    Box pos1;
    Box pos2;

    QuorridorBoard(int h, int w) {
        super(h,w);
        maxBorders = Math.max(h,w) - 1;
        pos1 = (Box) board[h-1][w/2];
        if (w % 2 != 0) {
            pos2 = (Box) board[0][w/2];
        } else {    
            pos2 = (Box) board[0][w/2-1];   
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
                if (((Box) board[row][c]) == pos1) {
                    System.out.print(ANSI_PURPLE_BACKGROUND + " X " + ANSI_RESET); 
                } else if (((Box) board[row][c]) == pos2) {
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

    public boolean validateBorder(Dot a , Dot b) {
        return true; //TODO
    }

    public boolean validateMove(String move) {
        return true; //TODO
    }

    public void drawBorder(Dot a, Dot b, Player player) {
        Dot mid = getMidpoint(a, b);

        Line line1 = getLineBetween(a,mid);
        line1.draw(player);

        Line line2 = getLineBetween(mid,b);
        line2.draw(player);

        // TODO: 
        // check if less than max borders
        // increment borders drawn by player
    }

    private Dot getMidpoint(Dot a , Dot b) {
        return null; //TODO
    }

    public void move(Player player, String direction) {
        System.out.printf("   "); //TODO
    }

    @Override
    public boolean solved() {
        return false; //TODO
    }


}
