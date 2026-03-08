package core;

import gamehandler.GameResult;
import iohandler.Input;

public abstract class Game {

    protected Input input;

    protected Player[] users;

    protected Game(Input input, int numPlayers) {
        this.input = input;
        this.users = new Player[numPlayers];
    }

    // protected abstract void initializeGame();
    // protected abstract void explainGame();

    public abstract GameResult play();
}