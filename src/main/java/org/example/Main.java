package org.example;
import javax.swing.JFrame;

public class Main extends JFrame {
    public Main() {

        setTitle("Hostel Management");

        setSize(512, 384);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    public static void main(String[] args) {
          new SignIn();

    }
}