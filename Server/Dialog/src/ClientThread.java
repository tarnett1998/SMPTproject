import javax.swing.*;
import java.net.*;
import java.io.*;

public class ClientThread extends Thread {
    private String ClientID;
    private Socket clientSocket;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private String[] dirArray = null;
    private String filePath = "";
    private JTextArea log;

    ClientThread(Socket clientSocket, JTextArea log) {
        this.clientSocket = clientSocket;
        this.log = log;
        ClientID = (this.clientSocket.getInetAddress().getHostAddress() + ":" + this.clientSocket.getPort());
    }

    public synchronized void run() {
        try {
            dos = new DataOutputStream(clientSocket.getOutputStream());
            dis = new DataInputStream(clientSocket.getInputStream());
            log.append("Connection Established w/Client: " + ClientID + "\n");
            filePath = new File(".").getCanonicalPath();
            while(true) {
                try {
                    String str = dis.readUTF();
                    log.append("<" + ClientID + "> Command: " + str + "\n");
                    dirArray = str.split("[ \t]+", 2);
                    str = dirArray[0].toUpperCase();
                    switch (str) {
                        case "HELO":
                            break;
                        default:
                            dos.writeUTF("ERROR: Unrecognized Command: " + dirArray[0]);
                            dos.flush();
                            log.append("ERROR: Unrecognized Command: " + dirArray[0] + "\n");
                            break;
                    }
                } catch(Exception e) {break;}
            }
        } catch (Exception e) {
            log.append("Exception (ClientThread): " + e + "\n");
        }
    }
}