package io.github.akumosstl.persistence;



import io.github.akumosstl.model.Action;

import java.sql.*;
import java.util.Map;

public class SqlitePersistence {

    public static void save(Map<Integer, Action> queue) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:recording.db")) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS actions (id INTEGER PRIMARY KEY, componentId TEXT, parentId TEXT, windowTitle TEXT, componentType TEXT, inputValue TEXT, delay INTEGER)");

            for (Map.Entry<Integer, Action> entry : queue.entrySet()) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO actions (id, componentId, parentId, windowTitle, componentType, inputValue, delay) VALUES (?, ?, ?, ?, ?, ?, ?)");
                Action a = entry.getValue();
                ps.setInt(1, entry.getKey());
                ps.setString(2, a.componentId);
                ps.setString(3, a.parentId);
                ps.setString(4, a.windowTitle);
                ps.setString(5, a.componentType);
                ps.setString(6, a.inputValue);
                ps.setLong(7, a.delay);
                ps.executeUpdate();
            }
            System.out.println("[SQLite] Saved recording.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
