package core;
public class Tile {

    protected int row;
    protected int col;

    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }  

    public void setRow(int r) {
        row = r;
    }

    public void setCol(int c) {
        col = c;
    }
}
