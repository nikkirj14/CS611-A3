package slidingpuzzle;

import iohandler.Input;
import core.Game;
import core.Board;
import core.Player;

public class SlidingPuzzleGame extends Game {
    
    public SlidingPuzzleGame(Input input, Player player) {
        super(input, 1); // GameManager constructor
        users[0] = player; // cannot be played by more than one 
    }

    // Fascilitates game play 
    @Override
    public Player play(){

        int maxSize = 20;
        int height = input.nextInt("Enter puzzle height: ", maxSize); 
        int width = input.nextInt("Enter puzzle width: ", maxSize); 

        Board board = new SlidingPuzzleBoard(height,width); 

        System.out.println("");
        System.out.println("DIRECTIONS: Slide blocks into the empty space to rearrange them into the correct");
        System.out.println("numerical order; continue until all blocks are in sequence!");
        System.out.println("");

        board.initialize(); //add output?
        
        board.print();

        int maxVal = height * width- 1;
        // while in not solved state continue to attempt moves
        while(!board.solved()) { 
            int move = checkMove(board,maxVal); 
        
            ((SlidingPuzzleBoard) board).slide(move);
            board.print(); //add output?
        }

        System.out.println("Congratulations!! You won!");
        return users[0];
    }

    // Gets input from player and validates move
    private int checkMove(Board board, int maxVal) {
        while (true) {
            int move = input.nextInt("Which tile do you want to slide? ", maxVal);

            if (((SlidingPuzzleBoard) board).validateMove(move)) {
                return move;
            }

            System.out.println("This tile cannot be moved! Try again.");
        }
    }

}