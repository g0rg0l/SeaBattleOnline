package self.gorgol.gameUtilities.ships;

import self.gorgol.gameUtilities.BoardCoordinates;
import self.gorgol.gameUtilities.BorderShipRectangle;

import java.util.ArrayList;

public abstract class Ship {
    public boolean isHorizontal;
    public BoardCoordinates position;
    public int length;
    protected final ArrayList<BoardCoordinates> positions = new ArrayList<>();
    protected final ArrayList<BoardCoordinates> deadCells = new ArrayList<>();

    public void init(BoardCoordinates position, boolean hz) {
        this.position = position;
        this.isHorizontal = hz;
        this.positions.clear();
        this.deadCells.clear();

        if (this.isHorizontal) {
            for (int i = 0; i < length; i++) {
                this.positions.add(new BoardCoordinates(position.x, position.y + i));
            }
        }
        else {
            for (int i = 0; i < length; i++) {
                this.positions.add(new BoardCoordinates(position.x + i, position.y));
            }
        }
    }

    public boolean containsPos(BoardCoordinates position) {
        for (BoardCoordinates coordinates : this.positions)
            if (position.equals(coordinates)) return true;

        return false;
    }

    public boolean intersecs(Ship ship) {
        for (BoardCoordinates shipPos : ship.positions) {
            if (this.containsPos(shipPos)) return true;
        }

        return false;
    }

    public boolean intersecsRectangle(BorderShipRectangle rectangle) {
        for (BoardCoordinates pos : this.positions) {
            if (rectangle.hasPosition(pos)) return true;
        }

        return false;
    }

    public void attack(BoardCoordinates position) {
        this.deadCells.add(position);
    }

    public boolean isDead() { return this.deadCells.size() == this.positions.size(); }

    public BoardCoordinates getMinPosition() { return this.positions.get(0); }
    public BoardCoordinates getMaxPosition() { return this.positions.get(this.positions.size() - 1); }
}
