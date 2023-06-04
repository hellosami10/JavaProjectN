package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.json.JSONArray;
import org.json.JSONObject;
public class SignIn extends JFrame {

    private JLabel lblUsername, lblPassword;
    public static JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnSignUp;



    public SignIn() {

        setTitle("Sign In");
        setSize(512, 384);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        lblUsername = new JLabel("Username:");
        lblPassword = new JLabel("Password:");

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        btnLogin = new JButton("Login");
        btnSignUp = new JButton("Sign Up");

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Method to be executed when the button is clicked
                auth(txtUsername.getText(), txtPassword.getText(), SignIn.this);
            }
        });

        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Method to be executed when the button is clicked
                new SignUp();
            }
        });

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(new JLabel());
        panel.add(btnLogin);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel("Don't Have An Account? SignUp!"));
        panel.add(btnSignUp);
        add(panel);

        setVisible(true);
    }

    private static void auth(String txtUsername, String txtPassword, JFrame frame ) {
        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get("src/main/java/org/example/users.json")));
            JSONArray jsonArray = new JSONArray(jsonStr);
            boolean is_found = false;
            String role = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");

                role = jsonObject.getString("role");

                if(username.equals(txtUsername) && password.equals(txtPassword)) {
                    is_found = true;

                            Logger log = new Logger(username, role, LocalDateTime.now());
                    log.Save();
                    break;
                }
            }
            if(is_found) {

                frame.dispose();

                switch (role) {
                    case "student":
                        new Student();
                        break;
                    case "admin":
                        new Admin();
                        break;
                    default:
                }
            } else {

                JOptionPane.showMessageDialog(frame, "User Not Found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}