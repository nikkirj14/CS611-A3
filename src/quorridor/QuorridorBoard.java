package quorridor;

import core.Player;
import grid.Box;
import grid.Direction;
import grid.Dot;
import grid.GridBoard;
import grid.Line;

import java.util.*;

public class QuorridorBoard extends GridBoard {

    protected int maxBorders;
    protected Box[] position;
    protected int borders1;
    protected int borders2;

    QuorridorBoard(int h, int w, Player[] players) {
        if (w % 2 == 0) {
            throw new IllegalArgumentException("W should be odd");
        }
        super(h,w);
        maxBorders = Math.max(h,w) - 1;
        position = new Box[2];
        position[0] = (Box) board[h-1][w/2];
        ((Box) board[h-1][w/2]).fill(players[0]);

        // odd width board
        position[1] = (Box) board[0][w/2];
        ((Box) board[0][w/2]).fill(players[1]);
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

    public int validateBorder(int player, Dot a , Dot b) {
        int rowDiff = Math.abs(a.getRow() - b.getRow());
        int colDiff = Math.abs(a.getCol() - b.getCol());

        //TODO:
        // borders cannot be placed on the ends of the board
        boolean isBoardHorizontalEnd = (rowDiff == 0 && (a.getRow() ==0 || a.getRow()==height));
        boolean isBoardVerticalEnd = (colDiff == 0 && (a.getCol() ==0 || a.getCol()==width));

        if (!isBoardHorizontalEnd && !isBoardVerticalEnd && ((rowDiff == 2 && colDiff == 0) || (rowDiff == 0 && colDiff == 2))) {
            Dot mid = getMidpoint(a,b);
            Line line1 = getLineBetween(a,mid);
            Line line2 = getLineBetween(mid,b);
            if (line1.isEmpty() && line2.isEmpty()) {

                if (willCompletelyBlockOpponent(player, a,mid,b)) {
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }
    // separated from validateMove so tryMove can test moves without a player
    public int validatePlayerMove(String direction, int player) {
        Box currPos = position[player];
        int r = currPos.getRow();
        int c = currPos.getCol();

        return validateMove(direction, r, c);
    }
    public int validateMove(String direction, int r, int c) {
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

        if (border.isDrawn()) return 1; // blocked by border
        if (((Box) board[newRow][newCol]).isFilled()) return 2; // tile occupied
        return 3; // valid
    }
    public boolean willCompletelyBlockOpponent(int player, Dot a, Dot b, Dot c) {
        int opponent = player == 0 ? 1 : 0;

        // Temporarily draw the border
        Line l1 = getLineBetween(a, c);
        Line l2 = getLineBetween(c, b);

        Player temp = new Player("dummy", -1);
        l1.draw(temp);
        l2.draw(temp);

        // Run BFS to see if opponent still has a path
        boolean reachable = bfsPath(opponent) != null;

        l1.clear();
        l2.clear();

        return !reachable;
    }
    private HashMap<String, Object> bfsPath(int opponent) {
        Box start = position[opponent];
        int goalRow = (opponent == 0 ? 0 : height - 1);
        boolean[][] visited = new boolean[height][width];
        Box[][] parent = new Box[height][width];
        String[][] moveFromParent = new String[height][width];
        Queue<Box> path = new LinkedList<>();

        path.add(start);
        visited[start.getRow()][start.getCol()] = true;

        while (!path.isEmpty()) {
            Box curr = path.poll();
            int r = curr.getRow();
            int c = curr.getCol();

            // Once you've reached the goal row, you know the path exists!
            if (r == goalRow) {
                HashMap<String, Object> output = new HashMap();
                output.put("goal", curr);
                output.put("visited", visited);
                output.put("parent", parent);
                output.put("moveFromParent", moveFromParent);
                return output;
            }

            // Try all 4 directions
            tryMove(path, visited, parent, moveFromParent, r, c, "U", opponent);
            tryMove(path, visited, parent, moveFromParent,r, c, "D", opponent);
            tryMove(path, visited, parent, moveFromParent, r, c, "L", opponent);
            tryMove(path, visited, parent, moveFromParent, r, c, "R", opponent);
        }
        return null;
    }
    public List<String> shortestPath(int player) {
        HashMap<String, Object> output = bfsPath(player);
        if (output == null) { // should never happen
            throw new NullPointerException("CPU cannot reach the end of the board.");
        }

        Box goal = (Box) output.get("goal");

        // Reconstruct path
        Box[][] parent = (Box[][]) output.get("parent");
        String[][] moveFromParent = (String[][]) output.get("moveFromParent");
        List<String> dirs = new ArrayList<>();
        Box curr = goal;
        Box start = position[player];
        while (curr != start) {
            int r = curr.getRow();
            int c = curr.getCol();
            String dir = moveFromParent[r][c];
            dirs.add(dir);
            System.out.println(r + "-" + c  + " dir " + dir + " dirs " + dir);
            System.out.println("Parents " + parent[r][c]);
            curr = parent[r][c];
        }
        Collections.reverse(dirs);
        return dirs;
    }

    private void tryMove(Queue<Box> path, boolean[][] visited, Box[][] parent, String[][] moveFromParent, int r, int c, String dir, int player) {
        int result = validateMove(dir,r,c);
        System.out.println(player + " " + dir + " " + result );

        if (result == 3) { // valid move
            int newRow = r, newCol = c;
            Line border = null;
            switch (dir) {
                case "U":
                    newRow--;
                    border = horizontalLines[r][c]; // up
                    break;
                case "D":
                    newRow++;
                    border = horizontalLines[r+1][c]; // down
                    break;
                case "L":
                    newCol--;
                    border = verticalLines[r][c];  // left
                    break;
                case "R":
                    newCol++;
                    border = verticalLines[r][c+1]; // right
                    break;
            }
            // bounds
            if (newRow < 0 || newRow >= height || newCol < 0 || newCol >= width) return;

            // if hit a border, return
            if (border.isDrawn()) return;

            if (!visited[newRow][newCol]) {
                visited[newRow][newCol] = true;
                parent[newRow][newCol] = (Box)board[r][c];
                moveFromParent[newRow][newCol] = dir;
                path.add((Box) board[newRow][newCol]);
            }
        }
    }
    public void drawBorder(Dot a, Dot b, Player player) {
        int playerId = player.getNumber();
        Dot mid = getMidpoint(a, b); // for borders that span 2 units

        Line line1 = getLineBetween(a, mid);
        line1.draw(player);

        Line line2 = getLineBetween(mid, b);
        line2.draw(player);

        // TODO:
        // check if less than max borders
        // increment borders drawn by player
        if (playerId == 1) {
            borders1++;
        }  else if (playerId == 2) {
            borders2++;
        }
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
        //TODO
        Box pos0 = position[0];
        Box pos1 = position[1];
        return (pos0.getRow() == 0 || pos1.getRow() == height-1);
    }
    public int getBordersLeft(int player) {
        switch (player) {
            case 0:
                return maxBorders-borders1;
            default:
                return maxBorders-borders2;
        }
    }
}
