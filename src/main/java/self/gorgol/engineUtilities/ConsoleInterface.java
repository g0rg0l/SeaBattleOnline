package self.gorgol.engineUtilities;

public class ConsoleInterface {
    public static final String EMPTY_CELL = " ";
    public static final String MISSED_CELL = "o";
    public static final String ATTACKED_CELL = "X";
    public static final String ANSI_WHITE_BACKGROUND_BRIGHT = "\u001B[0;100m";
    public static final String ANSI_BLACK_SQUARE = "\033[0;107m";
    public static final String ANSI_WHITE_SQUARE = "\u001B[47m";
    public static final String ANSI_RESET_COLOR = "\u001B[0m";
    public static final String ANSI_RED_COLOR = "\033[1;91m";
    public static final String ANSI_BLACK_COLOR = "\033[1;32m";
    public static final String[] HEADERS_LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    public static void messageIncorectInput() {
        System.out.println("Incorrect input, try again.");
    }

    public static void messageAlreadyAttackedPosition() {
        System.out.println("This possition has been attacked already. Enter new position.");
    }
}
