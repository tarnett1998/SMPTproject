import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private String ClientID;
    private Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private JTextArea log;

    private static String OK_CODE = "250";
    private static String DATA_HANDLER = "354";


    ClientThread(Socket clientSocket, JTextArea log) {
        this.clientSocket = clientSocket;
        this.log = log;
        ClientID = (this.clientSocket.getInetAddress().getHostAddress() + ":" + this.clientSocket.getPort());
        dos = null;
        dis = null;
    }

    public synchronized void run() {
        try {
            dos = new DataOutputStream(clientSocket.getOutputStream());
            dis = new DataInputStream(clientSocket.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dos));
            log.append("Connection Established w/Client: " + ClientID + "\n");
            while(true) {
                try {
                    String str = br.readLine();
                    log.append("<" + ClientID + "> Command: " + str + "\n");
                    switch (str) {
                        case "HELO":
                            bw.write("Helo " + ClientID + "! Nice to meet you!");
                            bw.flush();
                            log.append("Server: Helo " + ClientID + "! Nice to meet you!\n");
                            break;
                        case "FROM":
                            bw.write("OK");
                            bw.flush();
                            log.append("SERVER: Message from: " + ClientID + "\n");
                            break;
                        case "TO":
                            bw.write("OK");
                            bw.flush();
                            String recep = br.readLine();
                            log.append("SERVER: Message to: " + recep + "\n");
                        case "DATA":
                            bw.write("End data with <CR><LF>.<CR><LF>");
                            bw.flush();
                            while(true) {
                                String data = "";
                                try {
                                    data += br.readLine() + "\n";
                                } catch(Exception e) {break;}
                            }
                            break;
                        default:
                            dos.writeUTF("ERROR: Unrecognized Command: " + str);
                            dos.flush();
                            break;
                    }
                } catch(Exception e) {break;}
            }
        } catch (Exception e) {
            log.append("Exception (ClientThread): " + e + "\n");
        }
    }
}