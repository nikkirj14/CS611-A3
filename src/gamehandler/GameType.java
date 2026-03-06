package gamehandler;
public enum GameType {
    DOTS_AND_BOXES(1, 2),
    SLIDING_PUZZLE(1, 1),
    QUORRIDOR(1,2);

    private final int minPlayers;
    private final int maxPlayers;

    GameType(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers() { 
        return minPlayers; 
    }
    public int getMaxPlayers() {
        return maxPlayers; 
    }
}