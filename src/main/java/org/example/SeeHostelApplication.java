package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class SeeHostelApplication extends JFrame {
    private JTable dataTable;
    private JComboBox<String> statusComboBox;
    private JSONArray bookingsArray;

    public SeeHostelApplication() {
        setTitle("Hostel Applications");
        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"Username", "Room ID", "Pricing", "Card Number", "CVV", "Expiry Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return String.class; // Set the column type to String for "Status" column
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Allow editing only for the "Status" column
            }
        };
        dataTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        statusComboBox = new JComboBox<>(new String[]{"Approve", "Reject"});
        dataTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(statusComboBox));

        loadHostelData(model);
        addComboBoxListener();

        setLocationRelativeTo(null);
        add(panel);
        setVisible(true);
    }

    private void loadHostelData(DefaultTableModel model) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/org/example/bookings.json")));
            bookingsArray = new JSONArray(jsonContent);

            for (int i = 0; i < bookingsArray.length(); i++) {
                JSONObject bookingObject = bookingsArray.getJSONObject(i);
                String username = bookingObject.getString("username");
                String roomId = bookingObject.getString("roomId");
                String roomType = bookingObject.getString("roomType");

                int pricing = getPricingByRoomType(roomType);

                String cardNumber = bookingObject.getString("cardNumber");
                String cvv = bookingObject.getString("cvv");
                String expiryDate = bookingObject.getString("expiryDate");

                String status = bookingObject.optString("isApproved");

                Object[] rowData = {username, roomId, pricing, cardNumber, cvv, expiryDate, status};
                model.addRow(rowData);
                statusComboBox.setSelectedItem(status);
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

    private void addComboBoxListener() {
        statusComboBox.addActionListener(e -> {
            int selectedRow = dataTable.getSelectedRow();
            if (selectedRow != -1) {
                String selectedStatus = (String) statusComboBox.getSelectedItem();
                JSONObject bookingObject = bookingsArray.getJSONObject(selectedRow);
                String previousStatus = bookingObject.optString("isApproved");

                if (!selectedStatus.equals(previousStatus)) {
                    bookingObject.put("isApproved", selectedStatus);

                    try {
                        Files.write(Paths.get("src/main/java/org/example/bookings.json"), bookingsArray.toString().getBytes());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    if (selectedStatus.startsWith("Approve")) {
                        JOptionPane.showMessageDialog(this, "Approved");
                    } else if (selectedStatus.equals("Reject")) {
                        JOptionPane.showMessageDialog(this, "Rejected");
                    }
                }
            }
        });
    }



}
