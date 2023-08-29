package self.gorgol.engineUtilities;

import self.gorgol.gameUtilities.Board;
import static self.gorgol.engineUtilities.ConsoleInterface.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Engine {
    private boolean isRunning = false;
    private boolean isFirstPlayer = true;
    private final Board first_board = new Board();
    private final Board second_board = new Board();
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        this.isRunning = true;
        this.isFirstPlayer = true;
        this.first_board.init();
        this.second_board.init();
        runGameLoop();
    }

    private void stop() { this.isRunning = false; }

    private void runGameLoop() {
        while (this.isRunning) {
            printBoards(); // Printing game boards
            String input = scanner.nextLine(); // Getting first player input
            while (!proccessInput(input)) input = scanner.nextLine(); // Attacking entered position
            this.isFirstPlayer = false;

            printBoards(); // Printing game boards
            input = scanner.nextLine(); // Getting first player input
            while (!proccessInput(input)) input = scanner.nextLine(); // Attacking entered position
            this.isFirstPlayer = true;
        }
    }

    private void printBoards() {
        String firstStringBoard = this.first_board.boardToString(false);
        String secondStringBoard = this.second_board.boardToString(false);

        String[] firstBoard = firstStringBoard.split("\n");
        String[] secondBoard = secondStringBoard.split("\n");

        for (int i = 0; i < firstBoard.length; i++) {
            printANSILine(firstBoard[i] + "    " + secondBoard[i]);
        }
    }

    private boolean proccessInput(String input) {
        if (input.equals("exit")) { stop(); return true; }
        else {
            if (this.isFirstPlayer) return isCorrectAttackInput(input) && second_board.attackByInput(input.toUpperCase());
            else return isCorrectAttackInput(input) && first_board.attackByInput(input.toUpperCase());
        }
    }

    private boolean isCorrectAttackInput(String input) {
        Pattern p = Pattern.compile("[A-Ja-j][0-9]");
        Matcher m = p.matcher(input);

        if (m.matches()) return true;
        else {
            messageIncorectInput();
            return false;
        }
    }

    static void printANSILine(String s) {
        try {
            new ProcessBuilder("cmd", "/c", "echo " + s).inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
