package self.gorgol.online;

import self.gorgol.gameUtilities.Board;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static self.gorgol.engineUtilities.ConsoleInterface.messageIncorectInput;
import static self.gorgol.online.ServerToClientComands.*;

public class Server {
    private ServerSocket serverSocket;
    private final DataOutputStream[] outputStreams = new DataOutputStream[2];
    private int playersCount = 0;
    private final boolean[] playersIn = {false, false};

    private boolean isGameRunning = false;
    private int numberOfMovingPlayer = 0;

    private Board firstBoard;
    private Board secondBoard;

    public Server() {
        System.out.println("----- GAME SERVER HAS BEEN STARTED -----");
        try {
            serverSocket = new ServerSocket(59090);

        } catch (IOException exception) {
            System.out.println("Exeption while initiallization server");
        }
    }

    public void startAcceptingConnections() {
        AcceptorConnections acceptorConnections = new AcceptorConnections();
        acceptorConnections.start();
    }

    private void initGame() {
        this.firstBoard = new Board();
        this.secondBoard = new Board();

        firstBoard.init();
        secondBoard.init();

        this.isGameRunning = true;
    }

    private void stopGame() {
        this.isGameRunning = false;
    }

    private void nextMove() {
        this.numberOfMovingPlayer = (this.numberOfMovingPlayer == 0) ? 1 : 0;
    }

    private void showBoards() {
        sendMessageToPlayers(SHOW_BOARDS);

        /* Boards for #1 player */
        String stringFirstBoard = firstBoard.boardToString(true);
        String stringSecondBoard = secondBoard.boardToString(false);
        sendMessageToPlayer(stringFirstBoard, 1);
        sendMessageToPlayer(stringSecondBoard, 1);

        /* Boards for #2 player */
        stringFirstBoard = firstBoard.boardToString(false);
        stringSecondBoard = secondBoard.boardToString(true);
        sendMessageToPlayer(stringSecondBoard, 2);
        sendMessageToPlayer(stringFirstBoard, 2);
    }

    private boolean attackBoard(String input) {
        if (numberOfMovingPlayer == 0) {
            return isCorrectAttackInput(input) && secondBoard.attackByInput(input.toUpperCase());
        }
        else {
            return isCorrectAttackInput(input) && firstBoard.attackByInput(input.toUpperCase());
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

    private void startGameCommand() throws IOException {
        sendMessageToPlayers(PLAYERS_READY);
        System.out.println("There are two players. Game is starting...\n");
        showBoards();
    }

    private void stopGameCommand() {
        sendMessageToPlayers(GAME_INTERRUPTED);
        System.out.println("Game is stopped\n");
    }

    private void sendMessageToPlayers(String message) {
        for (DataOutputStream out : this.outputStreams) {
            if (out != null) {
                try {
                    out.writeUTF(message);
                } catch (IOException exception) {  }
            }
        }
    }

    private void sendMessageToPlayer(String message, int n) {
        try {
            this.outputStreams[n - 1].writeUTF(message);
        } catch (IOException exception) {  }
    }

    private class AcceptorConnections extends Thread {

        private void disconnectExtraClient(Socket extraClientSocket) {
            try {
                DataOutputStream out = new DataOutputStream(extraClientSocket.getOutputStream());
                out.writeUTF(EXTRA_PLAYER);

                out.close();
                extraClientSocket.close();
            } catch (IOException exception) {  }
        }

        public void run() {
            try {
                while (true) {
                    Socket playerSocket = serverSocket.accept();

                    if (playersCount < 2) {
                        System.out.println("Player #" + (++playersCount) + " has been just joined.");

                        /* Opening and starting new thread to recieve and send data from/to client detachted */
                        int playerNumber = (!playersIn[0]) ? 1 : 2;
                        playersIn[playerNumber - 1] = true;

                        ClientConnectionHandler newPlayerThread = new ClientConnectionHandler(playerSocket, playerNumber);
                        outputStreams[playerNumber - 1] = newPlayerThread.getOut();
                        newPlayerThread.start();

                        /* Checking for game starting */
                        if (playersCount == 2) {
                            initGame();
                            startGameCommand();
                        }
                    }
                    else {
                        disconnectExtraClient(playerSocket);
                    }
                }
            } catch (IOException exception) {  }
        }
    }

    private class ClientConnectionHandler extends Thread {
        private final Socket socket;
        private final DataInputStream in;
        private final DataOutputStream out;
        private final int number;
        private String name;

        public ClientConnectionHandler(Socket socket, int n) throws IOException {
            this.socket = socket;
            this.number = n;
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
        }

        public void run() {
            try {
                greetCommand();

                while (true) {
                    out.writeUTF("");
                    Thread.sleep(500);

                    if (isGameRunning && (number - 1) == numberOfMovingPlayer) {
                        moveCommand();
                        nextMove();
                        showBoards();
                    }
                }

            } catch (IOException exception) { closeConnection(); } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void closeConnection() {
            System.out.println("Player #" + this.number + " [" + this.name + "]" + " has leaved.");
            playersCount--;
            outputStreams[this.number - 1] = null;
            playersIn[this.number - 1] = false;
            if (isGameRunning) {
                stopGame();
                stopGameCommand();
            }

            try {
                this.socket.close();
                this.out.close();
                this.in.close();
            } catch (IOException exception) { System.out.println("Exeption");  }
        }

        private void greetCommand() throws IOException {
            out.writeUTF(GREET_MESSAGE);
            this.name = in.readUTF();
        }

        private void moveCommand() throws IOException {
            out.writeUTF(MOVE_MESSAGE);

            String stringPosition = in.readUTF();
            while (!attackBoard(stringPosition)) {
                out.writeUTF(WRONG_MOVE_MESSAGE);
                stringPosition = in.readUTF();
            }

        }

        public DataOutputStream getOut() { return this.out; }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startAcceptingConnections();
    }
}
