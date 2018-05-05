//GUI
import javax.swing.*;
//Event
import java.awt.*;
import java.awt.event.*;
//IO
import java.io.*;
//Util
import java.util.*;
//net
import java.net.*;

public class Client extends JFrame implements ActionListener{

   //Simple fields for sending message
   private JLabel jlFrom = new JLabel("From:");
   private JLabel jlTo = new JLabel("To:");
   private JLabel jlIP = new JLabel("IP:");
   private JLabel jlSubject = new JLabel("Subject:");
   private JLabel jlMessage = new JLabel("Message:");

   private JTextField jtfFrom = new JTextField(14);
   private JTextField jtfTo = new JTextField(14);
   private JTextField jtfIP = new JTextField(14);
   private JTextField jtfSubject = new JTextField(14);
   private JTextArea jtaMessage = new JTextArea(20,40);

   private JButton jbConnect = new JButton("Connect");

   //simple fields for recieveing message
   private JLabel jlFromR = new JLabel("From:");
   private JLabel jlToR = new JLabel("To:");
   private JLabel jlSubjectR = new JLabel("Subject:");
   private JLabel jlMessageR = new JLabel("Message content:");

   private JTextField jtfFromR = new JTextField(14);
   private JTextField jtfToR = new JTextField(14);
   private JTextField jtfSubjectR = new JTextField(14);
   private JTextArea jtaMessageR = new JTextArea(20,40);
   private JTextField jtfUsername = new JTextField(10);
   private JTextField jtfPassword = new JTextField(10);

   //java.net
   private Socket socket = null;
   private int PORT_NUMBER = 42069;
   private BufferedWriter out = null;
   private BufferedReader in = null;

   public static void main(String args[]){

      new Client();
   }

   private Client(){

      JPanel jpTop = new JPanel();
      jpTop.setLayout(new GridLayout(4,2));

         //Send side
         JPanel jpServer = new JPanel();
         jpServer.setLayout(new FlowLayout(FlowLayout.LEFT));
            jpServer.add(jlIP);
            jpServer.add(jtfIP);
            jpServer.add(jbConnect);

         JPanel jpTo = new JPanel();
         jpTo.setLayout(new FlowLayout(FlowLayout.LEFT));
            jpTo.add(jlTo);
            jpTo.add(jtfTo);

         JPanel jpFrom = new JPanel();
         jpFrom.setLayout(new FlowLayout(FlowLayout.LEFT));
            jpFrom.add(jlFrom);
            jpFrom.add(jtfFrom);

         JPanel jpSubject = new JPanel();
         jpSubject.setLayout(new FlowLayout(FlowLayout.LEFT));
            jpSubject.add(jlSubject);
            jpSubject.add(jtfSubject);

         //view side
         JPanel jpFromR = new JPanel();
         jpFromR.setLayout(new FlowLayout(FlowLayout.LEFT));
            jpFromR.add(jlFromR);
            jpFromR.add(jtfFromR);

         JPanel jpToR = new JPanel();
         jpToR.setLayout(new FlowLayout(FlowLayout.LEFT));
            jpToR.add(jlToR);
            jpToR.add(jtfToR);

         JPanel jpSubjectR = new JPanel();
         jpSubjectR.setLayout(new FlowLayout(FlowLayout.LEFT));
            jpSubjectR.add(jlSubjectR);
            jpSubjectR.add(jtfSubjectR);

      //4 rows by 2 columns
      jpTop.add(jpServer);  jpTop.add(new JPanel());
      jpTop.add(jpTo);      //jpTop.add(jpToR);
      jpTop.add(jpFrom);    //jpTop.add(jpFromR);
      jpTop.add(jpSubject); //jpTop.add(jpSubjectR);

      JPanel jpCenter = new JPanel();
      jpCenter.setLayout(new GridLayout(1,2));
      jpCenter.add(jtaMessage);
      jpCenter.add(jtaMessageR);

      JPanel jpSouth = new JPanel();
      jpSouth.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpSouth.add(jtfUsername);
      jpSouth.add(jtfPassword);

      this.add(jpSouth,BorderLayout.SOUTH);
      this.add(jpCenter, BorderLayout.CENTER);
      this.add(jpTop, BorderLayout.NORTH);

      jbConnect.addActionListener(this);

      setTitle("Client");
      setVisible(true);
      setSize(800,600);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocation(0,0);
   }

   public void actionPerformed(ActionEvent ae){

      switch(ae.getActionCommand()){
         case "Connect":
            doSend();
            break;
      }
   }

   private void doSend(){

      //Make a new message
      Message msg = new Message(jtfTo.getText(),jtfFrom.getText(),jtfSubject.getText(),jtaMessage.getText(),jtfIP.getText());

      if(msg.isValid()){

         //search message for period on own line
         //msg.parseMessage();


            System.out.println("--Valid--");

            try{
               //open socket and input and output streams
               socket = new Socket(jtfIP.getText(),PORT_NUMBER);

               out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
               in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

               System.out.println("got here");

               //write out login info
               out.write(jtfUsername.getText()+ "\n");
               out.flush();

               jtaMessageR.append(in.readLine() + "\n");
               //read a 250

               out.write(jtfPassword.getText() + "\n"); // working on an encryption for this as just handing out the password doesn't seem secure. Expect something by wednesday.
               out.flush();

               //read a 220
               jtaMessageR.append(in.readLine() + "\n");

               //write HELO command
               out.write("HELO relay.group_three.org" + "\n");
               out.flush();
               //System.out.println("HELO relay.group_three.org");

               //get 250
               jtaMessageR.append(in.readLine() + "\n");

               //write from
               out.write("FROM " + msg.getFrom() + "\n");
               out.flush();
               //System.out.println("FROM: <" + msg.getFrom() + ">");

               //get 250
               jtaMessageR.append(in.readLine() + "\n");

               //to who, EVERYONE IN CC ARRAY
               
               out.write("TO "+msg.getTo()+"\n");
               out.flush();

               //get 250
               jtaMessageR.append(in.readLine() + "\n");


               out.write("DATA"+"\n");
               out.flush();
               jtaMessageR.append(in.readLine() + "\n");
               jtaMessageR.setVisible(true);

               for(int i =0 ; i < msg.formatMessage().size(); i++) {
                    out.write(msg.formatMessage().get(i)+"\n");
                    out.flush();
               }

               //format the msg

               
//                for(int i = 0; i < msg.cc.size(); i++){
//                   String rcpt = "TO <" + msg.cc.get(i) + ">";
//                   out.write(rcpt + "\n");
//                   out.flush();
//                   System.out.println(rcpt);
// 
//                   jtaMessageR.append(in.readLine() + "\n");
//                }

               
               ////////////////////////

               //get 354
               jtaMessageR.append(in.readLine() + "\n");

               System.out.println("Message sent to server");
               //from etc.
//               ArrayList<String> arr = msg.formatMessage();
//               for(String x : arr){
//
//                  //write the headers
//                  out.write(x + "\n");
//                  out.flush();
//
//
//                  System.out.println(x);
//               }
               //System.out.println("End message sent to server");
               //end data

               //read 250
               //jtaMessageR.append(in.readLine() + "\n");

               //send quit
               out.write("QUIT" + "\n");
               out.flush();

               //read 221 goodbye
               jtaMessageR.append(in.readLine() + "\n");

            }catch(ConnectException ce){
               //cannot connect
               System.out.println("Could not connect to the server, is it on?");
            }catch(InterruptedIOException iioe){
               //network timeout
               System.out.println("Network timeout, looks like the server turned off");
            }catch(IOException ioe){
               //the rest
               ioe.printStackTrace();
            }

         }else{
            System.out.println("Invalid");
         }
   }
}
