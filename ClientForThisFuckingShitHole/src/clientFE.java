import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.awt.*;

public class clientFE extends JFrame implements ActionListener {

    private JPanel jpNorth = new JPanel();
    private JPanel jpRow0 = new JPanel();
    //top line
    private JLabel jlServer = new JLabel("Server: ");
    private JTextField jtfServer = new JTextField(15);
    private JButton jbConnect = new JButton("Connect");
    //bottom line
    private JTextArea jtaLog = new JTextArea();
    private JScrollPane jspLog = new JScrollPane(jtaLog);

    private BufferedReader br = null;
    private BufferedWriter bw = null;
    public Socket s = null;
   /*
      main method starts here, sole purpose is to call/create new of the FileClient constructor below.
   */
        public static void main(String [] args) {
            new clientFE();
        }//end of main

   /*
      FileClient is a constructor that builds the gui and uses action listeners on the various buttons
   */
   public clientFE() {
            this.setTitle("FTPClient [Mishra && Arnett]");
            this.setSize(400, 475);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //adds the two main containers to the JFrame
            this.add(jpNorth, BorderLayout.NORTH);
            this.add(jspLog, BorderLayout.CENTER);

            //sets layout and adds the rows of items to the grid
            jpNorth.setLayout(new GridLayout(2,0));
            jpNorth.add(jpRow0);

            //adds the server informations to top row
            jpRow0.add(jlServer);
            jpRow0.add(jtfServer);
            jpRow0.add(jbConnect);


            //action listeners for the various buttons
            jbConnect.addActionListener(this);


            this.setVisible(true);
        }//end of fileClient constructor


   /*
      Action performed method is used for all buttons, purpose is to make the connect, disconnect, list, cd, up, and down
      buttons have actions upon being clicked
   */
        public void actionPerformed(ActionEvent ae) {
            switch(ae.getActionCommand()){      //switch to destinguish between various buttons
                case "Connect":      //Connect button pressed
                    doConnect();      //calls the void doConnect method
                    jbConnect.setText("Disconnect");    //changes the button to disconnect
                    break;      //end of case
                case "Disconnect":   //disconnect button pressed
                    doDisconnect();   //calls the void doDisconnect method
                    jbConnect.setText("Connect"); //changes the button to connect
                    break;      //end of case
            }
        }//end of action performed method

   /*
      Connect method is called in order to handle the connect button actions.
      Connects to the ip and socket of the server and verifies that connection was
      successful, if it isnt, throws errors.
   */
        public void doConnect(){
            //trys to set the socket connects and create input/output streams
            try {
                s = new Socket(jtfServer.getText(),42069);
//                in = new DataInputStream(s.getInputStream());
//                out = new DataOutputStream(s.getOutputStream());
                br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                while(true) {bw.write("HELO");bw.flush();jtaLog.append(br.readLine());}
                
            }
            //if it fails to find the server, throw unknown host exception to log
            catch(UnknownHostException uhe) {
                jtaLog.append("Unable to connect to host. \n");
                return;
            }
            //if it cant communicate with host but sees host, there is an IO error, throws exception
            catch(IOException ie) {
                jtaLog.append("IOException communicating with host. \n");
                return;
            }
        }//end of doConnect

        public void doDisconnect(){
            //trys to close the socket and streams
            try{
                br.close();
                bw.close();
                //when it does, it throws exception and prints that it worked
            }catch(Exception e){
                jtaLog.append("Server disconnected.");
            }
        }//end of doDisconnect

}
