package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class Admin extends JFrame {
    private JButton btnAddHostelFrame;
    private JButton btnEditHostelFrame;
    private JButton btnDeleteHostelFrame;
    private JButton btnSeeHostelApplication;
    private JButton btnSeeStudentHistory;
    private JButton btnSeeUserLogs;
    private JButton btnExportCSV;
    private JButton btnSignOut;

    public Admin() {
        setTitle("Admin");
        setSize(512, 384);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1));
        btnAddHostelFrame = new JButton("Add Hostel");
        btnEditHostelFrame = new JButton("Edit Hostel");
        btnDeleteHostelFrame = new JButton("Delete Hostel");
        btnSeeHostelApplication = new JButton("See Hostel Application (Bookings)");
        btnSeeStudentHistory = new JButton("See Student Records");
        btnSeeUserLogs = new JButton("See User Login Logs");
        btnExportCSV = new JButton("ExportCSV");
        btnSignOut = new JButton("Sign Out");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnAddHostelFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new AddHostel();
            }
        });

        btnSignOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Admin.this.dispose();
                new SignIn();
            }
        });

        btnSeeUserLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new StudentLogShow();
            }
        });

        btnSeeStudentHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new SeeStudentHistory();
            }
        });

        btnSeeHostelApplication.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new SeeHostelApplication();
            }
        });

        btnEditHostelFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new EditHostel();
            }
        });

        btnDeleteHostelFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new DeleteHostel();
            }
        });

        btnExportCSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToCSV();
            }
        });

        panel.add(btnAddHostelFrame);
        panel.add(btnEditHostelFrame);
        panel.add(btnDeleteHostelFrame);
        panel.add(btnSeeHostelApplication);
        panel.add(btnSeeStudentHistory);
        panel.add(btnSeeUserLogs);
        panel.add(btnExportCSV);
        panel.add(btnSignOut);

        add(panel);
        setVisible(true);
    }

    private void exportToCSV() {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/org/example/bookings.json")));
            JSONArray bookingsArray = new JSONArray(jsonContent);

            FileWriter csvWriter = new FileWriter("src/main/java/org/example/report.csv");
            csvWriter.append("Room Type,Username,Pricing\n");

            for (int i = 0; i < bookingsArray.length(); i++) {
                JSONObject booking = bookingsArray.getJSONObject(i);

                String roomType = booking.getString("roomType");
                String username = booking.getString("username");
                int pricing = getPricingByRoomType(roomType);

                csvWriter.append(roomType);
                csvWriter.append(",");
                csvWriter.append(username);
                csvWriter.append(",");
                csvWriter.append(Integer.toString(pricing));
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

            JOptionPane.showMessageDialog(this, "CSV file exported successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to export CSV file.");
        }
    }
    private int getPricingByRoomType(String roomType) {
        switch (roomType) {
            case "single":
                return 3000;
            case "double":
                return 1500;
            case "dormitory":
                return 5000;
            default:
                return 0;
        }
    }


}
