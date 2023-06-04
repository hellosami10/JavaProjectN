package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class StudentBookHistory extends JFrame {
    private JTable dataTable;

    public StudentBookHistory() {
        setTitle("Booking History");

        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"Username", "Room ID", "Pricing", "Card Number", "CVV", "Expiry Date", "Approval"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadHostelData(model);

        setLocationRelativeTo(null);
        add(panel);
        setVisible(true);
    }

    private void loadHostelData(DefaultTableModel model) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/org/example/bookings.json")));
            JSONArray bookingsArray = new JSONArray(jsonContent);

            for (int i = 0; i < bookingsArray.length(); i++) {
                JSONObject bookingObject = bookingsArray.getJSONObject(i);
                if (bookingObject.getString("username").equals(SignIn.txtUsername.getText())) {
                    String roomId = bookingObject.getString("roomId");
                    String roomType = bookingObject.getString("roomType");
                    int pricing = getPricingByRoomType(roomType);
                    String cardNumber = bookingObject.getString("cardNumber");
                    String cvv = bookingObject.getString("cvv");
                    String expiryDate = bookingObject.getString("expiryDate");
                    String isApprovedText = "Not Yet";
                    if(bookingObject.getBoolean("isApproved")) {
                        isApprovedText = "Approved";
                    }
                    Object[] rowData = {SignIn.txtUsername.getText(), roomId, pricing, cardNumber, cvv, expiryDate, isApprovedText};
                    model.addRow(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
