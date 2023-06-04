package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EditHostel extends JFrame {

    private JButton btnUpdateHostel;

    private JLabel labelRoomId, labelRoomType, labelBalcony, labelFurniture, labelIsBooked;
    private JComboBox<Integer> comboBoxRoomId;
    private JTextField txtBalcony, txtFurniture;
    private JComboBox<String> comboBoxRoomType, comboIsBooked;

    private JSONArray hostelsData;

    public EditHostel() {
        setTitle("Edit Hostel");
        setSize(512, 384);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));


        labelRoomId = new JLabel("Room Id");

        labelRoomType = new JLabel("Room Type");
        labelBalcony = new JLabel("Balcony");
        labelFurniture = new JLabel("Furniture");
        labelIsBooked = new JLabel("Is Booked");

        comboBoxRoomId = new JComboBox<>();
        txtBalcony = new JTextField();
        txtFurniture = new JTextField();

        String[] roomTypes = {"single", "double", "dormitory"};
        comboBoxRoomType = new JComboBox<>(roomTypes);

        String[] isBookedOptions = {"yes", "no"};
        comboIsBooked = new JComboBox<>(isBookedOptions);

        btnUpdateHostel = new JButton("Update Hostel");
        btnUpdateHostel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHostel();
            }
        });
        panel.add(labelRoomId);
        panel.add(comboBoxRoomId);
        panel.add(labelRoomType);
        panel.add(comboBoxRoomType);
        panel.add(labelBalcony);
        panel.add(txtBalcony);
        panel.add(labelFurniture);
        panel.add(txtFurniture);
        panel.add(labelIsBooked);
        panel.add(comboIsBooked);
        panel.add(new JLabel());
        panel.add(btnUpdateHostel);
        add(panel);
        setVisible(true);

        loadHostelsData();

        populateRoomIds();

        comboBoxRoomId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillFieldsWithSelectedRoomData();
            }
        });


    }

    private void loadHostelsData() {
        String fileName = "src/main/java/org/example/hostels.json";

        try (FileReader fileReader = new FileReader(fileName)) {
            hostelsData = new JSONArray(new JSONTokener(fileReader));
        } catch (IOException e) {
            hostelsData = new JSONArray();
            e.printStackTrace();
        }
    }

    private void populateRoomIds() {
        comboBoxRoomId.addItem(null);

        for (int i = 0; i < hostelsData.length(); i++) {
            JSONObject hostel = hostelsData.getJSONObject(i);
            int roomId = hostel.getInt("roomId");
            comboBoxRoomId.addItem(roomId);
        }
    }

    private void clearFields() {
        comboBoxRoomId.setSelectedIndex(0);
        comboBoxRoomType.setSelectedIndex(0);
        txtBalcony.setText("");
        txtFurniture.setText("");
        comboIsBooked.setSelectedIndex(0);
    }



    private void fillFieldsWithSelectedRoomData() {
        int selectedRoomId = (int) comboBoxRoomId.getSelectedItem();

        for (int i = 0; i < hostelsData.length(); i++) {
            JSONObject hostel = hostelsData.getJSONObject(i);
            int roomId = hostel.getInt("roomId");
            if (roomId == selectedRoomId) {
                String roomType = hostel.getString("roomType");
                String balcony = hostel.getString("balcony");
                String furniture = hostel.getString("furniture");
                boolean isBooked = hostel.getBoolean("isBooked");

                String isBookedString = "no";
                if (isBooked == true) {
                    isBookedString = "yes";
                }
                comboBoxRoomType.setSelectedItem(roomType);
                txtBalcony.setText(balcony);
                txtFurniture.setText(furniture);
                comboIsBooked.setSelectedItem(isBookedString);
                break;
            }
        }
    }

    public void updateHostel() {
        int selectedRoomId = (int) comboBoxRoomId.getSelectedItem();

        for (int i = 0; i < hostelsData.length(); i++) {
            JSONObject hostel = hostelsData.getJSONObject(i);
            int roomId = hostel.getInt("roomId");
            if (roomId == selectedRoomId) {
                hostel.put("roomType", comboBoxRoomType.getSelectedItem());
                hostel.put("balcony", txtBalcony.getText());
                hostel.put("furniture", txtFurniture.getText());
                boolean isBookedBool = false;
                if(comboIsBooked.getSelectedItem().toString().toLowerCase() == "yes") {
                    isBookedBool = true;
                }
                hostel.put("isBooked", isBookedBool);
                break;
            }
        }

        String fileName = "src/main/java/org/example/hostels.json";

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(hostelsData.toString());
            JOptionPane.showMessageDialog(EditHostel.this, "Hostel Updated!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
