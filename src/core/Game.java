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
    public abstract GameResult play();
}