package gamehandler;
import core.Game;
import slidingpuzzle.SlidingPuzzleGame;
import dotsandboxes.DotsAndBoxesGame;
import iohandler.Input;
import quorridor.QuorridorGame;
import core.Player;

public class GameSession {

    private Input input;
    private Player[] players;

    public GameSession(Input input) {
        this.input = input;
    }

    public void start() {

        initializePlayers();

        boolean playing = true;

        while (playing) {

            GameType type = chooseGame();

            Player[] participants = chooseParticipants(type);

            Game game = initalizeGame(type, participants);

            Player winner = game.play();
            winner.incrementScore();
            
            announceScores();

            playing = askReplay();
        }
    }

    private GameType chooseGame() {

        System.out.println("1 - Sliding Puzzle");
        System.out.println("2 - Dots and Boxes");
        System.out.println("3 - Quorridor");

        while (true) {
            int choice = input.nextInt("Enter game choice: ", 3);

            switch (choice) {
                case 1:
                    return GameType.SLIDING_PUZZLE;

                case 2:
                    return GameType.DOTS_AND_BOXES;
                
                case 3:
                    if (players.length < 2) {
                        System.out.println("Two players needed for this game! Pick another game or restart session.");
                        continue;
                    }
                    return GameType.QUORRIDOR;

                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    public void initializePlayers() {

        int maxPlayers = 2;
        int minPlayers = 1;
        int count; 

        do {
            count = input.nextInt("Enter number of players (" + minPlayers + "-" + maxPlayers + "): ", maxPlayers);

        } while (count < minPlayers || count > maxPlayers);

        players = new Player[count];
        for (int i = 0; i < count; i++) {
            String name = input.nextLine("Enter name for Player " + (i + 1) + ": ");
            players[i] = new Player(name,i+1);
        }

    }

    private Player[] chooseParticipants(GameType gameType) {

        int min = gameType.getMinPlayers(); 
        int max = gameType.getMaxPlayers(); 
        int count = players.length;

        if (min != max & count != 1) {  
            while (true) {
                count = input.nextInt("How many players for this game? ", max); //(" + min + "-" + max + "): "

                if (count >= min && count <= max) {
                    break;
                }
            }
        }

        Player[] selected = new Player[count];

        boolean selectPlayersCase = (players.length > 1) &&
                           (gameType == GameType.SLIDING_PUZZLE ||
                            (gameType == GameType.DOTS_AND_BOXES && count < players.length));
        
        if (selectPlayersCase) {
            System.out.println("Available players:");
            for (int i = 0; i < players.length; i++) {
                System.out.println((i + 1) + " - " + players[i].getName());
            }

            for (int i = 0; i < max; i++) {
                while (true) {
                    int choice = input.nextInt("Select player by number: ", players.length) - 1;

                    if (choice >= 0 && choice < players.length) {
                        selected[i] = players[choice];
                        break;
                    }

                    System.out.println("Invalid selection. Try again.");
                }
            }
        } else {
            selected = players;
        }

        return selected;
    }

    private Game initalizeGame(GameType type, Player[] participants) {
        while (true) { 
            switch (type) {
                case SLIDING_PUZZLE:
                    return new SlidingPuzzleGame(input, participants[0]); 

                case DOTS_AND_BOXES:
                    return new DotsAndBoxesGame(input, participants);

                case QUORRIDOR:
                    return new QuorridorGame(input, participants);

                default:
                    throw new IllegalArgumentException("Unknown game type");
            }
        }
    }

    public void announceScores() {

        int width = 46;

        System.out.println();
        System.out.println("========= GAMES WON =========");

        StringBuilder line = new StringBuilder();
        for (int i = 0; i < players.length; i++) {
            line.append(players[i].getName()).append(": ").append(players[i].getScore());

            if (i < players.length - 1) {
                line.append(", ");
            }
        }

        System.out.printf("%" + ((width - line.length()) / 2) + "s%n", line);
        System.out.println("=============================");
        System.out.println();
    }

    public boolean askReplay() {
        while (true) {
            String answer = input.nextLine("Do you want to play another game? (Y/N) ");

            if (answer.equalsIgnoreCase("Y")) {
                return true;
            } 
            else if (answer.equalsIgnoreCase("N")) {
                System.out.println("Have a good day :D");
                return false;
            } 
            else {
                System.out.println("Invalid input! Please type Y or N.");
            }
        }
    }
}
