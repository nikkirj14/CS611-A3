package dotsandboxes;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Random;

import iohandler.Input;
import core.Game;
import core.Board;
import core.Player;
import grid.Dot;
import grid.Line;

public class DotsAndBoxesGame extends Game {


    protected int player1Score;
    protected int player2Score;
    
    public DotsAndBoxesGame(Input input, Player[] players) {
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

        System.out.println("");
        System.out.println("DIRECTIONS: Take turns drawing lines between two adjacent dots; scoring when you complete a box,"); 
        System.out.println("you claim it and get another turn; the player with the most boxes at the end wins! Ex: 'a1'");
        System.out.println("");
        board.print();

        int i = 0;
        while (!board.solved()) {
            System.out.printf("SCORE: %s: %d, %s: %d\n", users[0].getName(), player1Score, users[1].getName(), player2Score);
            Player current = users[i];
            int points = 0;
            points = handleTurn(board,current);
            if (points > 0) {
                if (current.getNumber() == 1) {
                    player1Score += points;
                } else {
                    player2Score += points;
                }
                continue;
            }

            i ^= 1; // switch to next user if no scored
        }
        
        boolean cpuPlayer = "CPU".equals(users[1].getName());

        System.out.printf("SCORE: %s: %d, %s: %d%n", users[0].getName(), player1Score, users[1].getName(), player2Score);
        if (player1Score > player2Score) {
            if (cpuPlayer) {
                System.out.printf("Congrats! You won!%n");
            } else {
                System.out.printf("%s won!\n", users[0].getName());
            }
            return users[0];
        } else if (player1Score < player2Score){
            if (cpuPlayer) {
                System.out.printf("CPU won this time :/ %n");
            } else {
                System.out.printf("%s won!\n", users[1].getName());
            }
            return users[1];
        } else {
            System.out.println("Tie! No points scored.");
            return null;
        }
    }


    private Board initBoard() {
        int maxSize = 10;
        int var1 = this.input.nextInt("Enter puzzle height: ", maxSize);
        int var2 = this.input.nextInt("Enter puzzle width: ", maxSize);
        return new DotsAndBoxesBoard(var1, var2); 
    }

    public int handleTurn(Board board, Player player) {
        
        while (true) {
            System.out.printf("%s's Move:\n", player.getName());

            if (player.getName() == "CPU") {
                return handleCPUTurn(board);
            }

            String startPoint = getStartInput();
            if (!((DotsAndBoxesBoard) board).validatePoint(startPoint)) {
                System.out.println("Invalid start point. Try again.");
                continue;
            }

            String endPoint = getEndInput();
            if (!((DotsAndBoxesBoard) board).validatePoint(endPoint)) {
                System.out.println("Invalid end point. Try again.");
                continue;
            }

            Dot startDot = ((DotsAndBoxesBoard) board).getDot(startPoint);
            Dot endDot =  ((DotsAndBoxesBoard) board).getDot(endPoint);
            
            if (! ((DotsAndBoxesBoard) board).validateMove(startDot, endDot)) {
                System.out.println("Invalid move. Try again.");
                continue;
            } 
            
            int scored = ((DotsAndBoxesBoard) board).draw(startDot, endDot, player);
            board.print();
            return scored;
        }
    }

    private int handleCPUTurn(Board board) {
        try {
            TimeUnit.SECONDS.sleep(1); // Wait for 1 second
        } catch (InterruptedException e) {
            // Handle the interruption
            Thread.currentThread().interrupt(); // Restore the interrupt flag
            System.err.println("The thread was interrupted during sleep.");
        }
    
         // Step 1: Look for almost complete box
        Line line = ((DotsAndBoxesBoard) board).tryAlmostCompleteBox();
        if (line != null) {
            int scored = ((DotsAndBoxesBoard) board).draw(line.getStart(),line.getEnd(),users[1]);
            board.print();
            return scored;
        }

        List<Line> availableLines = ((DotsAndBoxesBoard) board).getAvailableLines();


        if (availableLines.isEmpty()) {
            System.out.println("No available lines!");
            return 0;
        }
        Random random = new Random();
        Line chosen = availableLines.get(
            random.nextInt(availableLines.size())
        );

        int scored = ((DotsAndBoxesBoard) board).draw(chosen.getStart(), chosen.getEnd(), users[1]);
        board.print();
        return scored;
    }

    private String getStartInput() {
        
        return this.input.nextLine("Enter start point: ");

    }

    private String getEndInput() {
        
        return this.input.nextLine("Enter end point: ");

    } 
}
