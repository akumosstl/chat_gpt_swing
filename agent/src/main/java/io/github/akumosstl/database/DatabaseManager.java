package io.github.akumosstl.database;


import io.github.akumosstl.model.Action;

import java.sql.*;
import java.util.Map;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:agent.db";

    public DatabaseManager() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS actions (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "componentId TEXT, parentId TEXT, windowTitle TEXT," +
                            "componentType TEXT, inputValue TEXT, timeToNext INTEGER)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveQueue(Map<Integer, Action> queue) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO actions (componentId, parentId, windowTitle, componentType, inputValue, timeToNext) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            for (Action a : queue.values()) {
                ps.setString(1, a.componentId);
                ps.setString(2, a.parentId);
                ps.setString(3, a.windowTitle);
                ps.setString(4, a.componentType);
                ps.setString(5, a.inputValue);
                ps.setLong(6, a.timeToNext != null ? a.timeToNext : 0);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

