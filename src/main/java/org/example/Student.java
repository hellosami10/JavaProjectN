package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Student extends JFrame {

    private JButton btnBook, btnHistory, btnSignOut;

    public Student() {

        setTitle("Student");


        setSize(512, 384);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        btnBook = new JButton("Book Hostel Room");
        btnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                new BookHostel();

            }
        });

        btnHistory = new JButton("Booking History");
        btnSignOut = new JButton("Sign Out");

        btnSignOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                Student.this.dispose();
                new SignIn();

            }
        });

        btnHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                new StudentBookHistory();

            }
        });
        panel.add(btnBook);
        panel.add(btnHistory);
        panel.add(btnSignOut);
        add(panel);

        setVisible(true);
    }



}
