import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class serverFE extends JFrame implements ActionListener {


    private JButton initiate = new JButton("Start");

    private JTextArea log = new JTextArea(20, 65);

    private ServerThread sthread = null;

    private EncryptDecrypt ed;

    private serverFE() {

        Font font = new FontHandler().setFont1();

        this.setLocationRelativeTo(null);
        this.setTitle("Server Placeholder");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500,500);

        JPanel north = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel mid = new JPanel();

        DefaultCaret caret = (DefaultCaret)log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        north.add(initiate);
        this.add(north, BorderLayout.NORTH);

        JScrollPane jsp = new JScrollPane(log);
        mid.add(jsp);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(mid, BorderLayout.CENTER);
/*
        log.setEditable(false);*/

        log.setFont(font);
        initiate.setFont(font);
        initiate.setBackground(new Color(34,34,34));
        north.setBackground(new Color(54,54,54));
        mid.setBackground(new Color(54,54,54));
        log.setBackground(new Color(34,34,34));
        log.setForeground(new Color(221,221,221));
        initiate.setForeground(new Color(221,221,221));
        log.setLineWrap(true);
        log.setWrapStyleWord(true);

        initiate.addActionListener(this);

        this.pack();
        this.setVisible(true);

    }

    public static void main(String[] args) {
        new serverFE();
    }

    public void actionPerformed(ActionEvent ae) {
        switch(ae.getActionCommand()) {
            case "Start":
                sthread = new ServerThread(log);
                ed = new EncryptDecrypt(log.getText(), log);
                sthread.start();
                initiate.setText("Stop");
                break;

            case "Stop":
                sthread.kill();
                initiate.setText("Start");
                break;
        }
    }

}