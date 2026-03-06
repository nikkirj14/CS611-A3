package grid;
import core.Player;

public class Line {
    private final Dot start;
    private final Dot end;
    private boolean drawn;
    private Player drawnBy;

    public Line(Dot start, Dot end) {
        this.start = start;
        this.end = end;
        this.drawn = false;
        this.drawnBy = null;
    }

    public boolean isDrawn() {
        return drawn; 
    }

    public boolean isEmpty() {
        return !drawn; 
    }


    public void draw(Player player) {
        this.drawn = true;
        this.drawnBy = player;
        

    }

    public Dot getStart() {
        return start;
    }

    public Dot getEnd() {
        return end;
    }

    public Player getDrawnBy() {
        return drawnBy;
    }
}