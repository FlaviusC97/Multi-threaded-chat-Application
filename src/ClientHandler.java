import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Set<ClientHandler> clientHandlers;
    private File logFile;
    private ServerGUI serverGUI;
    private String userName;

    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers, File logFile, ServerGUI serverGUI) {
        this.clientSocket = socket;
        this.clientHandlers = clientHandlers;
        this.logFile = logFile;
        this.serverGUI = serverGUI;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = in.readLine();  // Citește numele de utilizator ca primul mesaj
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("logout")) {
                    clientHandlers.remove(this);
                    broadcastMessage(userName + " has logged out.");
                    break;
                }
                String formattedMessage = formatMessage(userName, message);
                broadcastMessage(formattedMessage);
                logMessage(formattedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessage(String message) {
        synchronized (clientHandlers) {
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.out.println(message);
            }
        }
        serverGUI.appendMessage(message);
    }

    private void logMessage(String message) {
        synchronized (logFile) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
                writer.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String formatMessage(String userName, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return "[" + timestamp + "] " + userName + ": " + message;
    }
}
