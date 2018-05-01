import javax.swing.*;
import java.io.*;
import java.net.Socket;

class HeloHandler {

    HeloHandler(Socket clientSocket, JTextArea log) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String cid = "<" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + ">";

        bw.write("OK");
        bw.flush();
        log.append("SERVER MSG: OK sent to client ID - " + cid + "\n");

        while(true) {
            try {

            } catch(Exception e) {
                return;
            }
        }
    }
}
