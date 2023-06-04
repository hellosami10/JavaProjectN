package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.jdesktop.swingx.JXDatePicker;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class BookHostel extends JFrame {
    private JComboBox<String> roomComboBox;
    private JTextField furnitureField;
    private JCheckBox bookedCheckbox;


    private JTextField balconyField;
    private JTextField roomTypeField;
    private JTextField usernameField;

    private JTextField cardNumberField;
    private JTextField cvvField;
    private JXDatePicker expiryDatePicker;
    private JSONArray roomsArray;

    public BookHostel() {
        setTitle("Room Booking");

        setSize(512, 384);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2, 10, 10));

        JLabel roomLabel = new JLabel("Room ID");
        roomComboBox = new JComboBox<>();
        loadRoomIds();
        roomComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRoomId = roomComboBox.getSelectedItem().toString();
                updateFields(selectedRoomId);
            }
        });
        panel.add(roomLabel);
        panel.add(roomComboBox);

        JLabel furnitureLabel = new JLabel("Furniture");
        furnitureField = new JTextField();
        furnitureField.setEditable(false);
        panel.add(furnitureLabel);
        panel.add(furnitureField);

        JLabel bookedLabel = new JLabel("Booked?");
        bookedCheckbox = new JCheckBox();
        bookedCheckbox.setEnabled(false);
        panel.add(bookedLabel);
        panel.add(bookedCheckbox);

        JLabel balconyLabel = new JLabel("Balcony");
        balconyField = new JTextField();
        balconyField.setEditable(false);
        panel.add(balconyLabel);
        panel.add(balconyField);

        JLabel roomTypeLabel = new JLabel("Room Type");
        roomTypeField = new JTextField();
        roomTypeField.setEditable(false);
        panel.add(roomTypeLabel);
        panel.add(roomTypeField);

        JLabel usernameLabel = new JLabel("Username");
        usernameField = new JTextField();
        usernameField.setEditable(false);
        panel.add(usernameLabel);
        panel.add(usernameField);

        JLabel cardNumberLabel = new JLabel("Card Number");
        cardNumberField = new JTextField();
        panel.add(cardNumberLabel);
        panel.add(cardNumberField);

        JLabel cvvLabel = new JLabel("CVV");
        cvvField = new JTextField();
        panel.add(cvvLabel);
        panel.add(cvvField);

        JLabel expiryDateLabel = new JLabel("Expiry Date");
        expiryDatePicker = new JXDatePicker();
        panel.add(expiryDateLabel);
        panel.add(expiryDatePicker);

        JButton bookButton = new JButton("Book");
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBooking();
            }
        });
        panel.add(bookButton);


        setLocationRelativeTo(null);
        add(panel);
        setVisible(true);
    }

    private void loadRoomIds() {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/org/example/hostels.json")));
            roomsArray = new JSONArray(jsonContent);
            roomComboBox.addItem("");
            for (int i = 0; i < roomsArray.length(); i++) {
                JSONObject roomObject = roomsArray.getJSONObject(i);
                String roomId = roomObject.getString("roomId");
                Boolean isBooked = roomObject.getBoolean("isBooked");
                if(isBooked == false) {
                    roomComboBox.addItem(roomId);
                }

            }

            roomComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedRoomId = roomComboBox.getSelectedItem().toString();
                    for (int i = 0; i < roomsArray.length(); i++) {
                        JSONObject roomObject = roomsArray.getJSONObject(i);
                        String roomId = roomObject.getString("roomId");

                        if (roomId.equals(selectedRoomId)) {
                            furnitureField.setText(roomObject.getString("furniture"));
                            bookedCheckbox.setSelected(roomObject.getBoolean("isBooked"));
                            balconyField.setText(roomObject.getString("balcony"));
                            roomTypeField.setText(roomObject.getString("roomType"));
                            usernameField.setText(SignIn.txtUsername.getText());


                            break;
                        }
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void updateFields(String roomId) {
        for (int i = 0; i < roomsArray.length(); i++) {
            JSONObject roomObject = roomsArray.getJSONObject(i);
            String currentRoomId = roomObject.getString("roomId");
            if (currentRoomId.equals(roomId)) {
                furnitureField.setText(roomObject.getString("furniture"));
                bookedCheckbox.setSelected(roomObject.getBoolean("isBooked"));
                balconyField.setText(roomObject.getString("balcony"));
                roomTypeField.setText(roomObject.getString("roomType"));

                break;
            }
        }
    }

    private void addBooking() {
        String selectedRoomId = roomComboBox.getSelectedItem().toString();
        String furniture = furnitureField.getText();
        boolean isBooked = bookedCheckbox.isSelected();
        String balcony = balconyField.getText();
        String roomType = roomTypeField.getText();
        String username = SignIn.txtUsername.getText();

        JSONObject bookingObject = new JSONObject();
        bookingObject.put("roomId", selectedRoomId);
        bookingObject.put("furniture", furniture);
        bookingObject.put("isBooked", true);
        bookingObject.put("balcony", balcony);
        bookingObject.put("roomType", roomType);
        bookingObject.put("username", username);
        bookingObject.put("cardNumber", cardNumberField.getText());
        bookingObject.put("cvv", cvvField.getText());
        bookingObject.put("expiryDate", expiryDatePicker.getDate().toString());

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/java/org/example/hostels.json")));
            JSONArray hostelsArray = new JSONArray(jsonContent);

            for (int i = 0; i < hostelsArray.length(); i++) {
                JSONObject hostelObject = hostelsArray.getJSONObject(i);
                String roomId = hostelObject.getString("roomId");
                if (roomId.equals(selectedRoomId)) {
                    hostelObject.put("isBooked", true);
                    break;
                }
            }

            FileWriter fileWriter = new FileWriter("src/main/java/org/example/hostels.json");
            fileWriter.write(hostelsArray.toString());
            fileWriter.close();

            String bookingsContent = new String(Files.readAllBytes(Paths.get("src/main/java/org/example/bookings.json")));
            JSONArray bookingsArray;
            if (bookingsContent.isEmpty()) {
                bookingsArray = new JSONArray();
            } else {
                bookingsArray = new JSONArray(bookingsContent);
            }
            bookingsArray.put(bookingObject);

            FileWriter bookingsFileWriter = new FileWriter("src/main/java/org/example/bookings.json");
            bookingsFileWriter.write(bookingsArray.toString());
            bookingsFileWriter.close();

            JOptionPane.showMessageDialog(this, "Payment Successful!");
            BookHostel.this.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
