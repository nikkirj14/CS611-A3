Required sections: Header, Files, Notes, How to compile and run, I/O Example


# CS611-Assignment 1
## Tic Tac Toe // Order and Chaos
---------------------------------------------------------------------------
- Name: Nikki Rejai
- Email: nikkirj@bu.edu
- Student ID: U64797160


## Files
---------------------------------------------------------------------------


src
- core 
    - Board - Defines the common structure and behavior of the game board.
    - Game - Abstract base class that defines the general game.
    - Player - Represents players in game session which play games.
    - Tile - Abstract base class that defines the components that populate the game board.
- dotsandboxes
    - Box implements Tile - Represents square that can be claimed by a player.
    - Dot - Represents a fixed point on the grid that lines connect between.
    - Line - Represents a drawable edge between two dots.
    - Direction - Enum representing possible directions for lines.
    - DotsAndBoxesBoard implements Board - Manages the grid structure, dots, lines, and boxes for the Dots and Boxes game.
    - DotsAndBoxesGame extends Game - Implements the rules and logic for Dots and Boxes.
- slidingpuzzle
    - Block implements Tile - Represents a movable numbered tile in the sliding puzzle.
    - SlidingPuzzleBoard implements Board - Manages the grid layout and movement logic of blocks.
    - SlidingPuzzleGame extends Game - Implements the rules and move validation for the sliding puzzle.
- iohandler
    - Input - Handles user input and abstracts console interaction from game logic.
- gamehandler
    - GameSession - Controls the overall lifecycle of a game session, including selecting and switching between games and keeping score.
    - GameType - Enum defining available game types and possible player counts.
- main
    - Main - Application entry point that starts the game session.

Puzzle.java: This class manages the puzzle state, the board layout and game rules. 
Block.java: This class models single units that make up the puzzle.
Main.java: This class is responsible for starting the game and getting user input.



## Notes
---------------------------------------------------------------------------
Design Decisions:
1. Chose to add GameSession class which manages entire session, which includes getting users at the beginning and then they can choose who plays either game and maintains score.
2. Block, Box extend Tile class - Both represent a single unit on a board, allowing boards to handle them polymorphically while encapsulating game-specific state.
3. DotsAndBoxesGame and SlidingPuzzleGame extend Game - They share a common lifecycle and allow GameSession to run any game through the same interface.
4. DotsAndBoxesBoard and SlidingPuzzleBoard implement Board - Each board provides a specific implementation for storing and managing tiles.
5. Added input class to abstract console interaction from game logic.
6. Added Direction enum to simplify logic in Board classes and GameType enum to and GameSession class respectively.

Extra:
7. Implemented CPU that can be played against if you choose 1 player option for dots and boxes.
8. Considered if 2 usrs wanted to compete against each other or CPU.

Sliding puzzle fixes: 
9.  Ensured the puzzle is solvable by working backwards from solved board,
10. Added validation for user input and setting capacity for board size
11. Added explanation for how to play game
12. Made use of inheritance in Block class, Player class, Game class, and SlidingPuzzle class


## How to compile and run
---------------------------------------------------------------------------

1. Navigate to the directory "611-A2/src" after unzipping the files
2. Run the following instructions:
avac --release 8 -d bin iohandler/*.java core/*.java main/*.java dotsandboxes/*.java slidingpuzzle/*.java gamehandler/*.java
java -cp bin main/Main


## Input/Output Example
---------------------------------------------------------------------------
Example of execution for Dots and Boxes game:


```
Enter number of players (1-2): 2
Enter name for Player 1: Nikki
Enter name for Player 2: Alex
Select one of the following games to play!
1 - Sliding Puzzle
2 - Dots and Boxes
Enter choice: 2
How many players for this game? 2
Enter puzzle height: 2
Enter puzzle width: 2
   1   2   3   
a  *   *   *
            
b  *   *   *
            
c  *   *   *
SCORE: Nikki: 0, Alex: 0
Nikki's Move:
Enter start point: a1
Enter end point: b1
   1   2   3   
a  *   *   *
   |        
b  *   *   *
            
c  *   *   *
SCORE: Nikki: 0, Alex: 0
Alex's Move:
Enter start point: b3
Enter end point: b2
   1   2   3   
a  *   *   *
   |        
b  *   *---*
            
c  *   *   *
SCORE: Nikki: 0, Alex: 0
Nikki's Move:
Enter start point: a3
Enter end point: a2
   1   2   3   
a  *   *---*
   |        
b  *   *---*
            
c  *   *   *
SCORE: Nikki: 0, Alex: 0
Alex's Move:
Enter start point: c2
Enter end point: c3
   1   2   3   
a  *   *---*
   |        
b  *   *---*
            
c  *   *---*
SCORE: Nikki: 0, Alex: 0
Nikki's Move:
etc..
```