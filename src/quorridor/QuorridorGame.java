package quorridor;

import dotsandboxes.DotsAndBoxesBoard;
import gamehandler.GameResult;
import grid.Line;
import iohandler.Input;
import core.Game;
import core.Board;
import core.Player;
import grid.Dot;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class QuorridorGame extends Game {
    protected int player1Score;
    protected int player2Score;
    
    public QuorridorGame(Input input, Player[] players) {
        super(input, 2); 
        if (players.length == 1) {
            users[0] = players[0];
            users[1] = new Player("CPU",2);
        } else {
            users = players;
        }
    }

    @Override
    public GameResult play() {
        QuorridorBoard board = (QuorridorBoard) initBoard();
        boolean quit = (board == null);

        // TODO: add directions
        if (quit){
            return new GameResult(GameResult.Type.QUIT, null);
        }
        board.print();
        int i = 1;
        while (!quit && !board.solved()) {
            i ^= 1;
            quit = handleTurn(board,i); // true if player quits
            // TO DO: maintain score?
        }
        if (quit){
            return new GameResult(GameResult.Type.QUIT, null);
        } else {
            return new GameResult(GameResult.Type.WIN, users[i]);
        }
    }

    private Board initBoard() {
        int maxSize = 10;
        int var1, var2;
        do {
            var1 = this.input.nextInt("Enter puzzle height (min = 3): ", maxSize);
        }while (var1 < 3 );
        // user chose to quit
        if (var1 == -1) {
            return null;
        }
        // since borders have a width=2, the board must have an odd width
        // to prevent the path from being blocked
        do {
            var2 = this.input.nextInt("Enter an odd-numbered puzzle width (min = 3): ", maxSize);
            // user chose to quit
            if (var2 == -1) {
                return null;
            }
        } while (var2 % 2 == 0 || var2 < 3);
        return new QuorridorBoard(var1, var2, users); 
    }

    private int getMoveType() {
        return this.input.nextInt("Select a move type - (1) move to adjacent tile, or (2) draw border: ",2);
    }

    private String getStartInput() {
        return this.input.nextLine("Enter start point or 1 to switch move: ");
    }

    private String getEndInput() {
        return this.input.nextLine("Enter end point or 1 to switch move: ");
    }

    public Player getPlayer(int index) {
        return users[index];
    }

    private String getDirection(boolean maxBordersReached) {
        while(true) {
            if (!maxBordersReached) {
                System.out.print("Enter 2 to switch move or");
            }
            String direction = this.input.nextLine("Enter direction left (L), right (R), up (U), down (D) to move: ").trim().toUpperCase();
            if (direction.equals("L") || direction.equals("R") ||
                direction.equals("U") || direction.equals("D") || direction.equals("q")|| (direction.equals("2") && !maxBordersReached)) {
                return direction;
            }
            System.out.print("Invalid direction. Please enter L, R, U, or D for directions ");
            if (!maxBordersReached) {
                System.out.println("or 2 to switch move.");
            }
        }

    }

    // only returns true when game is quit, otherwise false @ end of turn
    public boolean handleTurn(QuorridorBoard board, int player) {

        System.out.printf("%s's Move: %d borders left\n", users[player].getName(), board.getBordersLeft(player));
        if (users[player].getName() == "CPU") {
            handleCPUTurn(board);
            return false;
        }

        boolean maxBordersReached = board.getBordersLeft(player) == 0;
        // if max borders reached, force player to move
        int type;
        boolean moveSwitched = false;
        if (maxBordersReached) {
            System.out.println("Border limit reached. Player must move to adjacent tile.");
            type = 1;
        } else {
            type = getMoveType();
        }
        do {
            if (type == 1) { // moving
                while (true) {
                    String direction = getDirection(maxBordersReached);

                    // quit or switched to border move type
                    if (direction.equals("q")) {
                        return true;
                    } else if (!maxBordersReached && direction.equals("2")) {
                        type =2;
                        moveSwitched = true;
                        break;
                    }

                    int validation = (board).validatePlayerMove(direction, player);
                    if (validation == 0) {
                        System.out.println("Outside of board bounds. Try again.");
                    } else if (validation == 1) {
                        System.out.println("Blocked by border. Try again.");
                    } else if (validation == 2) {
                        System.out.println("Tile is occupied. Try again.");
                    } else {
                        board.move(player, users[player], direction);
                        moveSwitched = false;
                        board.print();
                        break;
                    }
                }
            } else if (type == -1) { // quit
                return true;
            } else { // drawing border
                while (true) {
                    String startPoint = getStartInput();

                    //quit
                    if (startPoint.equals("q")) {
                        return true;
                    }else if (startPoint.equals("1")) { // switch to moving
                        type =1;
                        moveSwitched = true;
                        break;
                    }
                    else if (!board.validatePoint(startPoint)) {
                        System.out.println("Invalid start point. Try again.");
                        continue;
                    }

                    String endPoint = getEndInput();
                    if (endPoint.equals("q")) {
                        return true;
                    }else if (endPoint.equals("1")) {
                        type =1;
                        moveSwitched = true;
                        break;
                    }
                    else if (!board.validatePoint(endPoint)) {
                        System.out.println("Invalid end point. Try again.");
                        continue;
                    }

                    Dot startDot = board.getDot(startPoint);
                    Dot endDot =  board.getDot(endPoint);

                    int validation = board.validateBorder(player, startDot, endDot);
                    if (validation == 0) {
                        System.out.println("Invalid border. Try again.");
                    } else if (validation == 2) {
                        System.out.println("Cannot completely block opponent. Try again.");
                    }else {
                        System.out.println("Border is valid");
                        board.drawBorder(startDot, endDot, users[player]);
                        board.print();
                        moveSwitched = false;
                        break;
                    }


                }
            }}while(moveSwitched);
        return false; // turn ended, player didn't quit
    }

    private void handleCPUTurn(QuorridorBoard board) {
        try {
            TimeUnit.SECONDS.sleep(1); // Wait for 1 second
        } catch (InterruptedException e) {
            // Handle the interruption
            Thread.currentThread().interrupt(); // Restore the interrupt flag
            System.err.println("The thread was interrupted during sleep.");
        }
        List<String> cpuPath = board.shortestPath(1);
        board.move(1, users[1], cpuPath.get(0)); // first item in cpuPath is the next move
        board.print();
    }
}
