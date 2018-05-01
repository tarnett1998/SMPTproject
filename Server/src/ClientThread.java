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
                System.out.println(str);
                if (str.substring(0, 4).equals("HELO")) {
                    String relay = str.substring(5);
                    bw.write("Hello " + relay + ", You are understood." + "\n");
                    bw.flush();
                    log.append("<" + clientID + "> Relay set to: " + relay+"\n");
                }
                if (str.substring(0, 4).equals("FROM")) {
                    String from = str.substring(5);
                    bw.write("250"+"\n");
                    bw.flush();
                    log.append("<" + clientID + "> Rcpt set to: " + from+"\n");
                }
                if (str.substring(0, 2).equals("TO")) {
                    String to = str.substring(3);
                    bw.write("250"+"\n");
                    bw.flush();
                    log.append("<" + clientID + "> Sender set to: " + to+"\n");
                }
                if (str.substring(0, 4).equals("DATA")) {
                    bw.write("354 End data with <CR><LF>.<CR><LF>" + "\n");
                    bw.flush();
                    String data = "";
                    while (true) {
                        try {
                            data += br.readLine() + "\n";
                        } catch (Exception e) {
                            bw.write("250: Queued as: null");
                            break;
                        }
                    }
                }
                if (str.substring(0, 4).equals("QUIT")) {
                    bw.write("221 Goodbye Client" + "\n");
                    bw.flush();
                    bw.close();
                    br.close();
                    this.kill();
                }




















//                switch (substr) {
//                    case "HEL": //helo
//                        if (str.substring(0,5).equals("HELO")) {
//                            bw.write("Helo " + clientID + "! Nice to meet you!\n");
//                            //bw.newLine();
//                            System.out.println("Shit3");
//                            bw.flush();
//                            System.out.println("Shit4");
//                            log.append("Server: Helo " + clientID + "! Nice to meet you!\n");
//                            System.out.println("Shit5");
//                        }
//                        break;
//                    case "FRO": //from
//                        if(str.substring(0,5).equals("FROM")) {
//                            String from = br.readLine();
//                            bw.write("OK" + "\n");
//                            bw.flush();
//                            log.append("SERVER: Message from: " + clientID + ": " + from + "\n");
//                            System.out.println("Shit6");
//                        }
//                        break;
//                    case "TO ": //to
//                        System.out.println("Shit7pt1");
//                        //bw.write("OK");
//                        //bw.flush();
//                        String recep = br.readLine();
//                        log.append("SERVER: Message to: " + recep + "\n");
//                        System.out.println("Shit7pt2");
//                        bw.write("OK" + "\n");
//                        bw.flush();
//                        break;
//                    case "DAT": //data
//                        if(str.substring(0,5).equals("DATA")) {
//                            System.out.println("Shit8");
//                            String data = "";
//                            while (true) {
//                                try {
//                                    data += br.readLine() + "\n";
//                                } catch (Exception e) {
//                                    break;
//                                }
//                            }
//                            bw.write("End data with <CR><LF>.<CR><LF>" + "\n");
//                            bw.flush();
//                            log.append("SERVER: Message data: " + clientID + ": " + data + "\n");
//                            System.out.println("Shit9");
//                        }
//                        break;
//                    case "QUIT":
//                    default:
//                        bw.write("ERROR: Unrecognized Command: " + str + "\n");
//                        bw.flush();
//                        break;
//                }
            } catch (Exception e) {
                break;
            }
        } catch (Exception e) {
            log.append("Exception (ClientThread): " + e + "\n");
        }
    }

    public void kill() {
        try{
            this.interrupt();
        }catch(Exception e){}
    }
}