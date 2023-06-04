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

public class DeleteHostel extends JFrame {

    private JButton btnDeleteHostel;

    private JLabel labelRoomId;
    private JComboBox<Integer> comboBoxRoomId;
    private JSONArray hostelsData;

    public DeleteHostel() {
        setTitle("Delete Hostel");
        setSize(512, 384);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));


        labelRoomId = new JLabel("Room Id");

        comboBoxRoomId = new JComboBox<>();

        btnDeleteHostel = new JButton("Delete Hostel");
        btnDeleteHostel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteHostel();
            }
        });

        panel.add(labelRoomId);
        panel.add(comboBoxRoomId);
        panel.add(new JLabel());
        panel.add(btnDeleteHostel);
        add(panel);
        setVisible(true);

        loadHostelsData();
        populateRoomIds();
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
        for (int i = 0; i < hostelsData.length(); i++) {
            JSONObject hostel = hostelsData.getJSONObject(i);
            int roomId = hostel.getInt("roomId");
            comboBoxRoomId.addItem(roomId);
        }
    }

    public void deleteHostel() {
        int selectedRoomId = (int) comboBoxRoomId.getSelectedItem();

        for (int i = 0; i < hostelsData.length(); i++) {
            JSONObject hostel = hostelsData.getJSONObject(i);
            int roomId = hostel.getInt("roomId");
            if (roomId == selectedRoomId) {
                hostelsData.remove(i);
                break;
            }
        }

        saveHostelsData();
        JOptionPane.showMessageDialog(DeleteHostel.this, "Hostel Deleted!");
        comboBoxRoomId.removeItem(selectedRoomId);
    }

    private void saveHostelsData() {
        String fileName = "src/main/java/org/example/hostels.json";

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(hostelsData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
