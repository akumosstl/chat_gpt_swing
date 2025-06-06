package io.github.akumosstl.persistence;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.akumosstl.model.Action;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveQueue(Map<Integer, Action> queue) {
        try (FileWriter writer = new FileWriter("queue.json")) {
            gson.toJson(queue, writer);
            System.out.println("[FileManager] Saved to queue.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
