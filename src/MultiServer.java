import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {

    private static final int PORT = 1234;
    private static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());
    private static File logFile = new File("resources/Data.txt");

    public static void main(String[] args) {
        ServerGUI serverGUI = new ServerGUI();
        serverGUI.setVisible(true);
        System.out.println("Server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientHandlers, logFile, serverGUI);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
