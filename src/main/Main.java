package main;
import gamehandler.GameSession;
import iohandler.Input;

public class Main {
    public static void main(String[] args) {

        Input input = new Input();

        GameSession session = new GameSession(input);
        session.start();

        input.close();
    }    
}
