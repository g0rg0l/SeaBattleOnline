package self.gorgol.gameUtilities;

import self.gorgol.gameUtilities.ships.Ship;
import java.util.ArrayList;

public class BorderShipRectangle {
    public final ArrayList<BoardCoordinates> cells = new ArrayList<>();

    public BorderShipRectangle(Ship ship, int boardSize) {
        int minX = (ship.isHorizontal) ? ship.position.x - 1 : ship.getMinPosition().x - 1;
        int maxX = (ship.isHorizontal) ? ship.position.x + 1 : ship.getMaxPosition().x + 1;
        int minY = (ship.isHorizontal) ? ship.getMinPosition().y - 1 : ship.position.y - 1;
        int maxY = (ship.isHorizontal) ? ship.getMaxPosition().y + 1 : ship.position.y + 1;

        minX = Math.max(minX, 0);
        maxX = Math.min(maxX, boardSize - 1);
        minY = Math.max(minY, 0);
        maxY = Math.min(maxY, boardSize - 1);

        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                BoardCoordinates pos = new BoardCoordinates(i, j);
                if (!ship.containsPos(pos)) this.cells.add(pos);
            }
        }
    }

    public boolean hasPosition(BoardCoordinates position) {
        for (BoardCoordinates pos : this.cells) {
            if (pos.equals(position)) return true;
        }

        return false;
    }
}
