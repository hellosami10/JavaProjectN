package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentLogShow extends JFrame {

    private JTable table;


    public StudentLogShow() {
        setTitle("Student Log");
        setSize(512, 384);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("User Name");
        tableModel.addColumn("Role");
        tableModel.addColumn("Logging TimeStamp");
        JSONArray jsonArray = Logger.GetAll();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String username = jsonObject.getString("username");
            String role = jsonObject.getString("role");
            String loginTimestamp = jsonObject.getString("loginTimestamp");

            tableModel.addRow(new Object[]{username, role, loginTimestamp});
        }
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane);
        add(panel);
        setVisible(true);
    }



}
