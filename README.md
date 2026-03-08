Required sections: Header, Files, Notes, How to compile and run, I/O Example


# CS611-Assignment 3
## Quorridor
---------------------------------------------------------------------------
- Names: Nikki Rejai, Sophia Tang
- Email: nikkirj@bu.edu, sstang@bu.edu

## Files
---------------------------------------------------------------------------
src
- core 
    - Board - Defines the common structure and behavior of the game board.
    - Game - Abstract base class that defines the general game.
    - Player - Represents players in game session which play games.
    - Tile - Abstract base class that defines the components that populate the game board.
- grid
  -  GridBoard implements Board - Defines the common structure and behavior of an advanced game board that uses dots, boxes, and lines.
  - Dot - Represents a fixed point on the grid that lines connect between.
  - Line - Represents a drawable edge between two dots.
  - Box implements Tile - Represents square that can be claimed by a player.
  - Direction - Enum representing possible directions for lines.
- dotsandboxes
    - DotsAndBoxesBoard implements GridBoard - Manages the grid structure, dots, lines, and boxes for the Dots and Boxes game.
    - DotsAndBoxesGame extends Game - Implements the rules and logic for Dots and Boxes.
- slidingpuzzle
    - Block implements Tile - Represents a movable numbered tile in the sliding puzzle.
    - SlidingPuzzleBoard implements Board - Manages the grid layout and movement logic of blocks.
    - SlidingPuzzleGame extends Game - Implements the rules and move validation for the sliding puzzle.
- quorridor
  - QuorridorBoard implements GridBoard - Manages the grid structure, dots, lines, and boxes for the Quorridor game.
    - QuorridorGame extends Game - Implements the rules and logic for Quorridor.
- iohandler
    - Input - Handles user input and abstracts console interaction from game logic.
- gamehandler
    - GameSession - Controls the overall lifecycle of a game session, including selecting and switching between games and keeping score.
    - GameType - Enum defining available game types and possible player counts.
    - GameResult - Object returned by Game objects at the conclusion of game to represent the ending state (win, tie, quit) and saves the winner(s).
- main
    - Main - Application entry point that starts the game session.

## Notes
---------------------------------------------------------------------------
Design Decisions:
1. Shifted DotsAndBoxes architecture & basic methods to abstract class GridBoard, which implements the Board interface.
- This allows DotsAndBoxesBoard and QuorridorBoard to share core organization, methods, and share classes like Dots, Direction, Box, and Line
- Offers extendability for future advanced board games.
- Extends Board behavior used in SlidingPuzzle
2. Added GameResult class to easily share and store game ending states.
- It is used across all games, reducing redundant logic
- Enhances extendability, as every new game added to this repo can more easily communicate ending state to the GameSession handler
3. QuorridorGame extends Game, allowing GameSession to easily run any game through the same interface.
4. Input class used in QuorridorGame to abstract console interaction from game logic.

Scalability:
6. Adjustable QuorridorBoard dimensions allows for a highly scalable game board encapsulating many game pieces.
7. One or multiple players are allowed to play the game together.
8. Games can be replayed infinitely.

Extra:
9. Implemented CPU that can be played against if you choose 1 player option for Quorridor.
10. Considered if 2 users wanted to compete against each other or CPU.
11. Added option to quit the game at any time.

## How to compile and run
---------------------------------------------------------------------------

1. Navigate to the directory "611-A3/src" after unzipping the files
2. Run the following instructions:
javac --release 8 -d bin iohandler/*.java core/*.java main/*.java dotsandboxes/*.java slidingpuzzle/*.java gamehandler/*.java grid/*.java quorridor/*.java
java -cp bin main/Main

## Input/Output Example
---------------------------------------------------------------------------
Example of execution for Quorridor game:

```
Welcome to the Game Session! Enter q at any time to quit the game.
Enter number of players (1-2): 2
Enter name for Player 1: Nikki
Enter name for Player 2: Alex
Select one of the following games to play!
1 - Sliding Puzzle
2 - Dots and Boxes
3 - Quorridor
Enter choice: 3
How many players for this game? 2
Enter puzzle height (min = 3): 3
Enter an odd-numbered puzzle width (min = 3): 3
Welcome to Quorridor!
DIRECTIONS: The objective of the game is to reach the other side of the board faster than your opponent.
Player 1 is purple and always starts at the bottom of the board, while Player 2 is blue and starts at the top of the board.
Each turn players can choose to move left, right, up, or down or place a border of width 2 to block the other opponent from reaching their side.
Players have a limit to the number of borders they can place. This is a 2 player game can be played against a CPU.
   1   2   3   4  
a  *   *   *   *
         X      
b  *   *   *   *
            
c  *   *   *   *
         X
d  *   *   *   *
Nikki's Move: 2 borders left
Select a move type - (1) move to adjacent tile, or (2) draw border: 1
Enter 2 to switch move or Enter direction left (L), right (R), up (U), down (D) to move: r
   1   2   3   4  
a  *   *   *   *
         X      
b  *   *   *   *
            
c  *   *   *   *
             X
d  *   *   *   *
Alex's Move: 2 borders left
Enter start point: c4
Enter end point: c2
Border is valid
   1   2   3   4  
a  *   *   *   *
         X      
b  *   *   *   *
            
c  *   *---*---*
             X
d  *   *   *   *
Nikki's Move: 2 borders left
Select a move type - (1) move to adjacent tile, or (2) draw border: 1
Enter 2 to switch move or Enter direction left (L), right (R), up (U), down (D) to move: u
Blocked by border. Try again.
Enter 2 to switch move or Enter direction left (L), right (R), up (U), down (D) to move: 2
Enter start point or 1 to switch move: b2
Enter end point or 1 to switch move: d4
Invalid border. Try again.
Enter start point or 1 to switch move: b2
Enter end point or 1 to switch move: d2
Cannot completely block opponent. Try again.
Enter start point or 1 to switch move: 1
Enter 2 to switch move or Enter direction left (L), right (R), up (U), down (D) to move: l
   1   2   3   4  
a  *   *   *   *
         X      
b  *   *   *   *
            
c  *   *---*---*
         X
d  *   *   *   *
Alex's Move: 1 borders left
Select a move type - (1) move to adjacent tile, or (2) draw border: 1
Enter 2 to switch move or Enter direction left (L), right (R), up (U), down (D) to move: l
   1   2   3   4  
a  *   *   *   *
     X      
b  *   *   *   *
            
c  *   *---*---*
         X
d  *   *   *   *
Nikki's Move:
etc..
```