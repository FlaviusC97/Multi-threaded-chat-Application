import javax.swing.*;
import java.io.*;
import java.net.*;

public class MultiClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        String userName = JOptionPane.showInputDialog("Enter your username:");
        if (userName != null && !userName.trim().isEmpty()) {
            ClientGUI clientGUI = new ClientGUI(SERVER_ADDRESS, SERVER_PORT, userName);
            clientGUI.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Username cannot be empty!");
        }
    }
}
