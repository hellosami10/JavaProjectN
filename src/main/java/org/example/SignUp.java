package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import org.json.*;
import java.util.ArrayList;
import java.util.List;

public class SignUp extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    private JSONArray userList;

    public SignUp() {
        userList = loadUsersFromFile(); // Load existing user data

        setTitle("User Registration");

        setLayout(new GridLayout(4, 2));
        setSize(512, 384);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role:");
        String[] roleOptions = {"student", "admin"};
        roleComboBox = new JComboBox<>(roleOptions);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();

                boolean usernameExists = checkUsernameExists(username);
                if (usernameExists) {
                    JOptionPane.showMessageDialog(SignUp.this, "Username already exists!");
                } else {
                    JSONObject newUser = new JSONObject();
                    newUser.put("username", username);
                    newUser.put("password", password);
                    newUser.put("role", role);
                    userList.put(newUser);
                    saveUsersToFile();
                    JOptionPane.showMessageDialog(SignUp.this, "User registered successfully!");
                }
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleComboBox);
        add(new JLabel()); // Placeholder for empty grid cell
        add(registerButton);


        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    private boolean checkUsernameExists(String username) {
        for (int i = 0; i < userList.length(); i++) {
            JSONObject user = userList.getJSONObject(i);
            if (user.getString("username").equals(username)) {
                return true;
            }
        }
        return false;
    }

    private JSONArray loadUsersFromFile() {
        JSONArray users = new JSONArray();
        try {
            File file = new File("src/main/java/org/example/users.json");
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    json.append(line);
                }
                br.close();

                users = new JSONArray(json.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void saveUsersToFile() {
        try {
            File file = new File("src/main/java/org/example/users.json");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(userList.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
