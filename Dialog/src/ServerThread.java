import javax.swing.*;
import java.net.*;

public class ServerThread extends Thread {

    private ServerSocket servSock = null;

    private JTextArea log;

    private Socket clientSocket = null;

    int ccon = 0;

    ServerThread(JTextArea log) {
        this.log = log;
    }

    public synchronized void run() {
        try {
            servSock = new ServerSocket(42069);
            clientSocket = null;
            for(;;) {
                try {
                    clientSocket = servSock.accept();
                    ClientThread cthread = new ClientThread(clientSocket, log);
                    cthread.start();
                    ccon = cthread.ccon;
                } catch(Exception e) {return;}
            }
        } catch (Exception ioe) {
            if (ioe.toString().contains("socket closed")) {
                return;
            }
            log.append("IOException (ServerThread[1]): " + ioe + "\n");
        }
    }

    void kill() {
        try {
            servSock.close();
            clientSocket.close();
        } catch (Exception e) {
            log.append("Exception (ServerThread[2]): " + e + "\n");
        }
    }

}
