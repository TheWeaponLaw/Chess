import view.ChessGameFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Enter the game");
        jFrame.setVisible(true);
        jFrame.setLayout(null);
        jFrame.setSize(500,350);
        jFrame.setLocationRelativeTo(new Container());
        jFrame.setBackground(Color.WHITE);

        JLabel jLabel = new JLabel();
        jLabel.setText("Welcome to play the chess");
        jLabel.setBackground(Color.WHITE);
        jLabel.setLocation(110,55);
        jLabel.setSize(300,100);
        jLabel.setFont(new Font("Rockwell",Font.BOLD,20));
        jLabel.setForeground(Color.RED);


        Button button = new Button("PVP");
        button.setLocation(180,120);
        button.setSize(100, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 10));


        Button button1 = new Button("PVE");;
        button1.setLocation(180,160);
        button1.setSize(100, 30);
        button1.setFont(new Font("Rockwell", Font.BOLD, 10));
        button1.setBackground(Color.WHITE);



//        ImageIcon imageIcon=new ImageIcon(".\\Enter.jpg");
//        JLabel jLabel1 = new JLabel(imageIcon);
//        jLabel1.setSize(600,600);
//        jLabel1.setLocation(-50,-140);
        jFrame.add(button);
        jFrame.add(jLabel);
        jFrame.add(button1);
//        jFrame.add(jLabel1,JLayeredPane.DEFAULT_LAYER);

        button.addActionListener(e -> {
            jFrame.dispose();
            ChessGameFrame mainFrame = new ChessGameFrame(1000, 760);
            mainFrame.setVisible(true);
        });
        button1.addActionListener(e -> {
            jFrame.dispose();
            ChessGameFrame mainFrame = new ChessGameFrame(1000, 760);
            mainFrame.setVisible(true);
            mainFrame.getGameController().getChessboard().pve =1;
        });
    }
}
