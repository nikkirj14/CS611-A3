package gamehandler;

import core.Player;

public class GameResult {
    public enum Type { WIN, TIE, QUIT }
    private final Type type;
    private final Player winner;

    public GameResult(Type type, Player winner) {
        if (type != Type.WIN && winner != null) {
            throw new IllegalArgumentException("Winner cannot be null");
        }
        this.type = type;
        this.winner = winner;
    }

    public Type getType() { return type; }
    public Player getWinner() { return winner; }
}

