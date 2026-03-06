package quorridor;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Random;

import iohandler.Input;
import core.Game;
import core.Board;
import core.Player;
import dotsandboxes.Dot;
import dotsandboxes.DotsAndBoxesBoard;

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
    public Player play() {
        Board board = initBoard(); 

        // TODO: add directions

        board.print();

        int i = 1;
        while (!board.solved()) {
            i ^= 1; 
            handleTurn(board,users[i]); // TO DO: maintain score?
        }
        return users[i];

    }

    private Board initBoard() {
        int maxSize = 10;
        int var1 = this.input.nextInt("Enter puzzle height: ", maxSize);
        int var2 = this.input.nextInt("Enter puzzle width: ", maxSize);
        return new QuorridorBoard(var1, var2); 
    }

    private int getMoveType() {
        
        return this.input.nextInt("Select a move type - (1) move to adjacent tile, or (2) draw border: ",2);

    }

    
    private String getStartInput() {
        
        return this.input.nextLine("Enter start point: ");

    }

    private String getEndInput() {
        
        return this.input.nextLine("Enter end point: ");

    }

    private String getDirection() {
        while(true) {
            String direction = this.input.nextLine("Enter direction left (L), right (R), up (U), down (D) to move: ").trim().toUpperCase();
            if (direction.equals("L") || direction.equals("R") ||
                direction.equals("U") || direction.equals("D")) {
                return direction;
            }

            System.out.println("Invalid direction. Please enter L, R, U, or D.");
        }

    }

    public void handleTurn(Board board, Player player) {
        System.out.printf("%s's Move:\n", player.getName());
            
        // if (player.getName() == "CPU") {
        //     return handleCPUTurn(board);
        // }

        int type = getMoveType();
        if (type == 1) { // moving 
            while (true) {
                String direction = getDirection();
                if (! ((QuorridorBoard) board).validateMove(direction)) {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }
                ((QuorridorBoard) board).move(player, direction);
                    
                board.print();
                return;
            }
        } else { // drawing border
            while (true) {
                String startPoint = getStartInput();
                if (!((QuorridorBoard) board).validatePoint(startPoint)) {
                    System.out.println("Invalid start point. Try again.");
                    continue;
                }

                String endPoint = getEndInput();
                if (!((QuorridorBoard) board).validatePoint(endPoint)) {
                    System.out.println("Invalid end point. Try again.");
                    continue;
                }

                Dot startDot = ((QuorridorBoard) board).getDot(startPoint);
                Dot endDot =  ((QuorridorBoard) board).getDot(endPoint);
                    
                if (! ((QuorridorBoard) board).validateBorder(startDot, endDot)) {
                    System.out.println("Invalid move. Try again.");
                    continue;
                } 
                ((QuorridorBoard) board).drawBorder(startDot, endDot, player);
                    
                board.print();
                return;
            } 
        } 
    }



}
