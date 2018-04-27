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

    int ccon = 0;


    ClientThread(Socket clientSocket, JTextArea log) {
        this.clientSocket = clientSocket;
        this.log = log;

        dos = null;
        dis = null;
    }

    public synchronized void run() {
        try {
            ccon += 1;
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            ClientID = (this.clientSocket.getInetAddress().getHostAddress() + ":" + this.clientSocket.getPort());
            System.out.println("Shit1");
            log.append("Connection Established w/Client: " + ClientID + "\n");
            while(true) {
                try {
                    String str = br.readLine();
                    log.append("<" + ClientID + "> Command: " + str + "\n");
                    System.out.println("Shit2");
                    switch (str) {
                        case "HELO":
                            bw.write("Helo " + ClientID + "! Nice to meet you!");
                            bw.newLine();
                            System.out.println("Shit3");
                            bw.flush();
                            System.out.println("Shit4");
                            log.append("Server: Helo " + ClientID + "! Nice to meet you!\n");
                            System.out.println("Shit5");
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
                            break;
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
                            bw.write("ERROR: Unrecognized Command: " + str);
                            bw.flush();
                            break;
                    }
                } catch(Exception e) {break;}
            }
        } catch (Exception e) {
            log.append("Exception (ClientThread): " + e + "\n");
        }
    }
}