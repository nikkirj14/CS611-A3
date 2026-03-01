package core;
public class Player {
    private int score; 
    private String name;
    private int number;

    public Player(String name, int number) {
        this.score = 0;
        this.name = name;
        this.number = number;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
    public int getNumber() {
        return number;
    }

    public void incrementScore() {
        score++;
    }
}
