package io.github.akumosstl.persistence;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.akumosstl.model.Action;

import java.io.FileWriter;
import java.util.Map;

public class JsonPersistence {
    public static void save(Map<Integer, Action> queue) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter("recording.json");
            gson.toJson(queue, writer);
            writer.close();
            System.out.println("[JSON] Saved recording.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
