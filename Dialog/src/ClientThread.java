import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket clientSocket;
    private JTextArea log;

    private static String OK_CODE = "250";
    private static String DATA_HANDLER = "354";

    private int ccon;


    ClientThread(Socket clientSocket, JTextArea log, int ccon) {
        this.clientSocket = clientSocket;
        this.log = log;
        this.ccon = ccon;

        DataOutputStream dos = null;
        DataInputStream dis = null;
    }

    public synchronized void run() {
        try {
            ccon += 1;
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String clientID = (this.clientSocket.getInetAddress().getHostAddress() + ":" + this.clientSocket.getPort());
            System.out.println("Shit1");
            log.append("Connection Established w/Client: " + clientID + "\n");
            while(true) try {
                String str = br.readLine();
                log.append("<" + clientID + "> Command: " + str + "\n");
                System.out.println("Shit2");
                switch (str) {
                    case "HELO":
                        bw.write("Helo " + clientID + "! Nice to meet you!");
                        bw.newLine();
                        System.out.println("Shit3");
                        bw.flush();
                        System.out.println("Shit4");
                        log.append("Server: Helo " + clientID + "! Nice to meet you!\n");
                        System.out.println("Shit5");
                        break;
                    case "FROM":
                        bw.write("OK");
                        bw.flush();
                        log.append("SERVER: Message from: " + clientID + "\n");
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
                        while (true) {
                            String data = "";
                            try {
                                data += br.readLine() + "\n";
                            } catch (Exception e) {
                                break;
                            }
                        }
                        break;
                    case "QUIT":
                    default:
                        bw.write("ERROR: Unrecognized Command: " + str);
                        bw.flush();
                        break;
                }
            } catch (Exception e) {
                break;
            }
        } catch (Exception e) {
            log.append("Exception (ClientThread): " + e + "\n");
        }
    }
}