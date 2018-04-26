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
    private Socket s = null;


        public static void main(String [] args) {
            new clientFE();
        }//end of main

   public clientFE() {
            this.setTitle("FTPClient [Mishra && Arnett]");
            this.setSize(400, 475);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.add(jpNorth, BorderLayout.NORTH);
            this.add(jspLog, BorderLayout.CENTER);

            jpNorth.setLayout(new GridLayout(2,0));
            jpNorth.add(jpRow0);

            jpRow0.add(jlServer);
            jpRow0.add(jtfServer);
            jpRow0.add(jbConnect);


            jbConnect.addActionListener(this);


            this.setVisible(true);
        }


        public void actionPerformed(ActionEvent ae) {
            switch(ae.getActionCommand()){      //switch to destinguish between various buttons
                case "Connect":      //Connect button pressed
                    doConnect();      //calls the void doConnect method
                        //changes the button to disconnect
                    break;      //end of case
                case "Disconnect":   //disconnect button pressed
                    doDisconnect();   //calls the void doDisconnect method
                    //changes the button to connect
                    break;      //end of case
            }
        }

        public void doConnect(){
            try {
                s = new Socket(jtfServer.getText(),42069);

                bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                System.out.println("got here1");
                bw.write("HELO");
                bw.newLine();
                bw.flush();
                System.out.println("got here2");
                    String reply = br.readLine();
                    jtaLog.append(reply+"\n");
                br.close();
                bw.close();
                System.out.println("got here3");
                jbConnect.setText("Disconnect");
            }
            catch(Exception e){
                System.out.println("Problem here"+e);
                doDisconnect();
            }
        }

        public void doDisconnect(){
            try{
                jbConnect.setText("Connect");
                br.close();
                bw.close();
                s.close();

            }catch(Exception e){
                jtaLog.append("Server disconnected.");
            }
        }
}
