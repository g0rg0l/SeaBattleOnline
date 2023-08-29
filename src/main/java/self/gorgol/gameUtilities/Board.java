package self.gorgol.gameUtilities;

import java.util.ArrayList;
import java.util.Random;
import self.gorgol.gameUtilities.ships.*;
import static self.gorgol.engineUtilities.ConsoleInterface.*;

public class Board {
    private final int boardSize = 10;

    private final ArrayList<Ship> ships = new ArrayList<>();
    private final ArrayList<BoardCoordinates> attackedPositions = new ArrayList<>();

    public void init() {
        Ship[] shipsToInit = {
                new Carrier(),
                new Battleship(),
                new Cruiser(),
                new Submarine(),
                new Destroyer()
        };

        for (Ship ship : shipsToInit) {
            ship.init(getRandomPlaceForShip(), getRandomDirectionForShip());
            while (!isCorrectShipPosition(ship)) { ship.init(getRandomPlaceForShip(), getRandomDirectionForShip()); }
            this.ships.add(ship);
        }
    }

    public String boardToString(boolean showShips) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < boardSize + 1; i++) {
            for (int j = 0; j < boardSize + 1; j++) {
                /* Заливка полей таблицы */
                if (i == 0 && j == 0) stringBuilder.append("   ");
                else if (j == 0)
                    stringBuilder.append(getColorizedBackground(HEADERS_LETTERS[i - 1], new BoardCoordinates(i, j)));
                else if (i == 0)
                    stringBuilder.append(getColorizedBackground(String.valueOf(j - 1), new BoardCoordinates(i, j)));

                /* Заливка ячеек таблицы */
                else {
                    BoardCoordinates currentPosition = new BoardCoordinates(i - 1, j - 1);
                    String cellSprite = getStringCell(currentPosition, showShips);
                    stringBuilder.append(getColorizedBackground(cellSprite, new BoardCoordinates(i, j)));
                }
                stringBuilder.append(ANSI_RESET_COLOR);
            }
            if (i != boardSize) stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public boolean attackByInput(String playerInput) {
        BoardCoordinates positionToAttack = new BoardCoordinates(
                getLetterIndexFromStringInput(playerInput),
                Character.getNumericValue(playerInput.charAt(1))
        );

        return attack(positionToAttack);
    }

    private boolean attack(BoardCoordinates position) {
        if (isNotAttackedPosition(position)) {
            Ship shipToAttack = getShipByPosition(position);
            if (shipToAttack != null) {
                shipToAttack.attack(position);
                if (shipToAttack.isDead()) fillAfterDeath(shipToAttack);
            }

            this.attackedPositions.add(position);

            return true;
        }
        else {
            messageAlreadyAttackedPosition();
            return false;
        }
    }

    private boolean isCorrectShipPosition(Ship ship) {
        BoardCoordinates minPos = ship.getMinPosition();
        BoardCoordinates maxPos = ship.getMaxPosition();

        if (minPos.x >= 0 && minPos.y >= 0 && maxPos.x <= boardSize - 1 && maxPos.y <= boardSize - 1) {
            for (Ship sh : this.ships) {
                if (ship.intersecs(sh)) return false;
            }

            BorderShipRectangle borders = new BorderShipRectangle(ship, this.boardSize);
            for (Ship sh : this.ships) {
                if (sh.intersecsRectangle(borders)) return false;
            }

            return true;
        }
        else return false;
    }

    private BoardCoordinates getRandomPlaceForShip() {
        Random random = new Random();

        return new BoardCoordinates(
                random.nextInt(0, 10),
                random.nextInt(0, 10)
        );
    }

    private boolean getRandomDirectionForShip() {
        Random random = new Random();

        return random.nextBoolean();
    }

    private void fillAfterDeath(Ship deadShip) {
        BorderShipRectangle deadRectangle = new BorderShipRectangle(deadShip, this.boardSize);

        for (BoardCoordinates pos : deadRectangle.cells)
            if (isNotAttackedPosition(pos)) this.attackedPositions.add(pos);
    }

    private String getColorizedBackground(String str, BoardCoordinates pos) {
        final String cellText = " " + str + " ";

        if (pos.x == 0 || pos.y == 0) return ANSI_WHITE_BACKGROUND_BRIGHT + cellText;

        return ((pos.x + pos.y) % 2 == 0) ?
                ANSI_BLACK_SQUARE + cellText :
                ANSI_WHITE_SQUARE + cellText;
    }

    private String getStringCell(BoardCoordinates pos, boolean tips) {
        if (tips) {
            if (isNotAttackedPosition(pos)) {
                if (isShipPosition(pos)) return ANSI_BLUE_COLOR + "@";
                else return EMPTY_CELL;
            }
            else {
                if (isShipPosition(pos)) return ANSI_RED_COLOR + ATTACKED_CELL;
                else return ANSI_BLACK_COLOR + MISSED_CELL;
            }
        }
        else {
            if (isNotAttackedPosition(pos)) return EMPTY_CELL;
            else {
                if (isShipPosition(pos)) return ANSI_RED_COLOR + ATTACKED_CELL;
                else return ANSI_GREEN_COLOR + MISSED_CELL;
            }
        }
    }

    private boolean isShipPosition(BoardCoordinates pos) {
        for (Ship ship : this.ships)
            if (ship.containsPos(pos)) return true;

        return false;
    }

    private Ship getShipByPosition(BoardCoordinates pos) {
        for (Ship ship : this.ships)
            if (ship.containsPos(pos)) return ship;
        return null;
    }

    private boolean isNotAttackedPosition(BoardCoordinates pos) {
        for (BoardCoordinates p : this.attackedPositions)
            if (pos.equals(p)) return false;

        return true;
    }

    private int getLetterIndexFromStringInput(String input) {
        String index = String.valueOf(input.charAt(0));

        for (int i = 0; i < HEADERS_LETTERS.length; i++) {
            if (HEADERS_LETTERS[i].equals(index)) return i;
        }

        return -1;
    }
}
