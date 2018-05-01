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
            String usr = br.readLine();
            log.append("<" + clientID + "> Command: " + usr + "\n");
            bw.write("250" + "\n");
            bw.flush();
            String pass = br.readLine();
            log.append("<" + clientID + "> Command: " + pass + "\n");
            bw.write("220" + "\n");
            bw.flush();
            System.out.println("Shit2");
            while(true) try {
                String str = br.readLine();
                switch (str) {
                    case "HELO":
                        bw.write("Helo " + clientID + "! Nice to meet you!\n");
                        //bw.newLine();
                        System.out.println("Shit3");
                        bw.flush();
                        System.out.println("Shit4");
                        log.append("Server: Helo " + clientID + "! Nice to meet you!\n");
                        System.out.println("Shit5");
                        break;
                    case "FROM":
                        String from = br.readLine();
                        bw.write("OK" + "\n");
                        bw.flush();
                        log.append("SERVER: Message from: " + clientID + ": " + from + "\n");
                        System.out.println("Shit6");
                        break;
                    case "TO":
                        System.out.println("Shit7pt1");
                        //bw.write("OK");
                        //bw.flush();
                        String recep = br.readLine();
                        log.append("SERVER: Message to: " + recep + "\n");
                        System.out.println("Shit7pt2");
                        bw.write("OK" + "\n");
                        bw.flush();
                        break;
                    case "DATA":
                        
                        System.out.println("Shit8");
                        String data = "";
                        while (true) {
                            try {
                                data += br.readLine() + "\n";
                            } catch (Exception e) {
                                break;
                            }
                        }
                        bw.write("End data with <CR><LF>.<CR><LF>" + "\n");
                        bw.flush();
                        log.append("SERVER: Message data: " + clientID + ": " + data + "\n");
                        System.out.println("Shit9");
                        break;
                    case "QUIT":
                    default:
                        bw.write("ERROR: Unrecognized Command: " + str + "\n");
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