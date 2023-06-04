package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private String _username;
    private String _role;
    private String _loginTimestamp;



    public Logger(String username, String role, LocalDateTime loginTimestamp) {
        _username = username;
        _role = role;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        _loginTimestamp = loginTimestamp.format(formatter);
    }

    public void Save() {
        JSONObject json = new JSONObject();
        json.put("username", _username);
        json.put("role", _role);
        json.put("loginTimestamp", _loginTimestamp);

        String fileName = "src/main/java/org/example/log.json";

        JSONArray jsonArray;

        try (FileReader fileReader = new FileReader(fileName)) {
            jsonArray = new JSONArray(new JSONTokener(fileReader));
            jsonArray.put(json);
        } catch (IOException e) {
            jsonArray = new JSONArray();
            jsonArray.put(json);
        }

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(jsonArray.toString());
            System.out.println("JSON data has been appended to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static JSONArray GetAll() {

        JSONArray jsonArray = null;
        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get("src/main/java/org/example/log.json")));
            jsonArray = new JSONArray(jsonStr);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }




}
