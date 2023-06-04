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

public class AddHostel extends JFrame  {


    private JButton btnAddHostel;

    private JLabel labelRoomId, labelRoomType, labelBalcony, labelFurniture, labelIsBooked;
    private JTextField txtRoomId, txtBalcony, txtFurniture;
    private JComboBox<String> comboBox, comboIsBooked;

    public AddHostel() {
        setTitle("Add Hostel");
        setSize(512, 384);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));


        labelRoomId = new JLabel("Room Id");

        labelRoomType = new JLabel("Room Type");
        labelBalcony = new JLabel("Balcony");
        labelFurniture = new JLabel("Furniture");
        labelIsBooked = new JLabel("Is Booked");

        txtRoomId = new JTextField();

        txtBalcony = new JTextField();
        txtFurniture = new JTextField();

        String[] items = new String[0];
        comboBox = new JComboBox<>(items);

        comboBox.addItem("single");
        comboBox.addItem("double");
        comboBox.addItem("dormitory");

        comboIsBooked = new JComboBox<>(items);

        comboIsBooked.addItem("yes");
        comboIsBooked.addItem("no");


        btnAddHostel = new JButton("Add Hostel");
        btnAddHostel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddHostel();
            }
        });
        panel.add(labelRoomId);
        panel.add(txtRoomId);
        panel.add(labelRoomType);
        panel.add(comboBox);
        panel.add(labelBalcony);
        panel.add(txtBalcony);
        panel.add(labelFurniture);
        panel.add(txtFurniture);
        panel.add(labelIsBooked);
        panel.add(comboIsBooked);
        panel.add(new JLabel());
        panel.add(btnAddHostel);
        add(panel);
        setVisible(true);
    }



    public void AddHostel() {
        String newRoomId = txtRoomId.getText();
        String furniture = txtFurniture.getText();
        boolean isBookedBool = comboIsBooked.getSelectedItem().toString().equalsIgnoreCase("yes");
        String balcony = txtBalcony.getText();
        String roomType = comboBox.getSelectedItem().toString();

        JSONObject newHostel = new JSONObject();
        newHostel.put("roomId", newRoomId);
        newHostel.put("furniture", furniture);
        newHostel.put("isBooked", isBookedBool);
        newHostel.put("balcony", balcony);
        newHostel.put("roomType", roomType);

        String fileName = "src/main/java/org/example/hostels.json";

        JSONArray jsonArray;

        try (FileReader fileReader = new FileReader(fileName)) {
            jsonArray = new JSONArray(new JSONTokener(fileReader));

            boolean roomExists = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject hostel = jsonArray.getJSONObject(i);
                String existingRoomId = hostel.getString("roomId");
                if (existingRoomId.equals(newRoomId)) {
                    roomExists = true;
                    break;
                }
            }

            if (roomExists) {
                JOptionPane.showMessageDialog(AddHostel.this, "Room ID already exists!");
            } else {
                jsonArray.put(newHostel);

                try (FileWriter fileWriter = new FileWriter(fileName)) {
                    fileWriter.write(jsonArray.toString());
                    JOptionPane.showMessageDialog(AddHostel.this, "Hostel Added!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            jsonArray = new JSONArray();
            jsonArray.put(newHostel);

            try (FileWriter fileWriter = new FileWriter(fileName)) {
                fileWriter.write(jsonArray.toString());
                JOptionPane.showMessageDialog(AddHostel.this, "Hostel Added!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}


