package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class SeeStudentHistory extends JFrame {
    private JTable dataTable;

    public SeeStudentHistory() {
        setTitle("All Student Records");

        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"Username", "Role"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadUserData(model);

        setLocationRelativeTo(null);
        add(panel);
        setVisible(true);
    }

    private void loadUserData(DefaultTableModel model) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/org/example/users.json")));
            JSONArray usersArray = new JSONArray(jsonContent);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                String username = userObject.getString("username");
                String role = userObject.getString("role");

                Object[] rowData = {username, role};
                model.addRow(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
