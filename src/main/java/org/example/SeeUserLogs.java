package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class SeeUserLogs extends JFrame {
    private JTable dataTable;

    public SeeUserLogs() {
        setTitle("All User Logs");

        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"Username", "Role", "Login Timestamp"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadLogData(model);

        setLocationRelativeTo(null);
        add(panel);
        setVisible(true);
    }

    private void loadLogData(DefaultTableModel model) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/org/example/log.json")));
            JSONArray logArray = new JSONArray(jsonContent);

            for (int i = 0; i < logArray.length(); i++) {
                JSONObject logObject = logArray.getJSONObject(i);
                String username = logObject.getString("username");
                String role = logObject.getString("role");
                String loginTimestamp = logObject.getString("loginTimestamp");

                Object[] rowData = {username, role, loginTimestamp};
                model.addRow(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
