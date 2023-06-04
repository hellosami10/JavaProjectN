package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class SeeHostelApplication extends JFrame {
    private JTable dataTable;
    private JCheckBox isApproved;
    private JSONArray bookingsArray;

    public SeeHostelApplication() {
        setTitle("Hostel Applications");

        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"Username", "Room ID", "Pricing", "Card Number", "CVV", "Expiry Date", "Is Approved"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return Boolean.class; // Set the column type to Boolean for "Is Approved" column
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Allow editing only for the "Is Approved" column
            }
        };
        dataTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        isApproved = new JCheckBox(); // No need to set the label here since it will be shown in the JTable
        dataTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(isApproved)); // Set a checkbox editor for "Is Approved" column

        loadHostelData(model);
        addCheckBoxListener();

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
                boolean isApprovedValue = bookingObject.getBoolean("isApproved"); // Read the initial value of "isApproved" property

                Object[] rowData = {username, roomId, pricing, cardNumber, cvv, expiryDate, isApprovedValue};
                model.addRow(rowData);
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

    private void addCheckBoxListener() {
        isApproved.addActionListener(e -> {
            int selectedRow = dataTable.getSelectedRow();
            if (selectedRow != -1) {
                boolean isChecked = isApproved.isSelected();
                JSONObject bookingObject = bookingsArray.getJSONObject(selectedRow);
                bookingObject.put("isApproved", isChecked); // Update the "isApproved" property in the JSON object

                // Write the updated JSON content back to the file
                try {
                    Files.write(Paths.get("src/main/java/org/example/bookings.json"), bookingsArray.toString().getBytes());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Show message based on checkbox status
                if (isChecked) {
                    JOptionPane.showMessageDialog(this, "Approved");
                } else {
                    JOptionPane.showMessageDialog(this, "Rejected");
                }
            }
        });
    }

}
