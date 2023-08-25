package self.gorgol.gameUtilities;

public class BoardCoordinates {
    public final int x;
    public final int y;

    public BoardCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(BoardCoordinates obj) {
        return obj.x == this.x && obj.y == this.y;
    }
}
