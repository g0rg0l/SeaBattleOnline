package self.gorgol.online;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import static self.gorgol.online.ServerToClientComands.*;

public class Player {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private boolean connectToServer() {
        try {
            socket = new Socket("192.168.0.108", 59090);
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());

            System.out.println("You are successfully connected to server.");

            return true;

        } catch (IOException exception) {
            System.out.println("Error while connecting to server. Please, try again.");

            return false;
        }
    }

    private void startRecievingMessages() {
        while (true) {
            try {
                String serverAnswer = in.readUTF();
                if (!serverAnswer.isEmpty()) {
                    proccessServerMessage(serverAnswer);
                }

            } catch (IOException exception) {  }
        }
    }

    private void proccessServerMessage(String message) throws IOException {
        switch (message) {
            case GREET_MESSAGE:
                greet();
                break;

            case PLAYERS_READY:
                playersReady();
                break;

            case GAME_INTERRUPTED:
                gameInterrupted();
                break;

            case MOVE_MESSAGE:
                move(false);
                break;

            case WRONG_MOVE_MESSAGE:
                move(true);
                break;

            case SHOW_BOARDS:
                showBoards();
                break;

            default:
                break;
        }
    }

    private void greet() throws IOException {
        System.out.println(GREET_MESSAGE);

        System.out.print("Enter your name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        this.out.writeUTF(name);
    }

    private void playersReady() {
        System.out.println(PLAYERS_READY);
    }

    private void gameInterrupted() {
        System.out.println(GAME_INTERRUPTED);
        System.out.println("You are already in waiting to play new game.");
    }

    private void move(boolean wrong) throws IOException {
        System.out.println(wrong ? WRONG_MOVE_MESSAGE : MOVE_MESSAGE);

        Scanner scanner = new Scanner(System.in);
        String inputOfAttack = scanner.nextLine();
        out.writeUTF(inputOfAttack);
    }

    private void showBoards() throws IOException {
        String firstStringBoard = in.readUTF();
        String secondStringBoard = in.readUTF();

        String[] firstBoard = firstStringBoard.split("\n");
        String[] secondBoard = secondStringBoard.split("\n");

        for (int i = 0; i < firstBoard.length; i++) {
            printANSILine(firstBoard[i] + "    " + secondBoard[i]);
        }
    }

    static void printANSILine(String s) {
        try {
            new ProcessBuilder("cmd", "/c", "echo " + s).inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Player player = new Player();

        if (player.connectToServer()) {
            player.startRecievingMessages();
        }
    }
}
