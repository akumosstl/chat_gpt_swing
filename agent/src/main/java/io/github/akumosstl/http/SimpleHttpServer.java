package io.github.akumosstl.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.github.akumosstl.automation.Recorder;
import io.github.akumosstl.automation.AutomationExecutor;
import io.github.akumosstl.model.Action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SimpleHttpServer {

    private final Recorder recorder;
    private HttpServer server;

    public SimpleHttpServer(Recorder recorder) {
        this.recorder = recorder;
    }

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/start-record", this::handleStartRecord);
        server.createContext("/stop-record", this::handleStopRecord);
        server.createContext("/automation", this::handleAutomation);

        server.setExecutor(null);
        server.start();

        System.out.println("HTTP Server started on port " + port);
    }

    public void stop() {
        server.stop(0);
    }

    private void handleStartRecord(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Recorder.startRecording();
            sendResponse(exchange, "Recording started.");
        } else {
            sendMethodNotAllowed(exchange);
        }
    }

    private void handleStopRecord(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Recorder.stopRecording();
            sendResponse(exchange, "Recording stopped and saved.");
        } else {
            sendMethodNotAllowed(exchange);
        }
    }

    private void handleAutomation(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String body = "";//new String(exchange.getRequestBody().toString().readAllBytes(), StandardCharsets.UTF_8);

            Gson gson = new Gson();
            Map<Integer, Action> queue = gson.fromJson(
                    body,
                    new com.google.gson.reflect.TypeToken<Map<Integer, Action>>() {
                    }.getType()
            );

            AutomationExecutor executor = new AutomationExecutor(queue);
            //new Thread(executor::execute).start();

            sendResponse(exchange, "Automation started.");
        } else {
            sendMethodNotAllowed(exchange);
        }
    }

    private void sendResponse(HttpExchange exchange, String responseText) throws IOException {
        byte[] bytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void sendMethodNotAllowed(HttpExchange exchange) throws IOException {
        String response = "Method Not Allowed";
        exchange.sendResponseHeaders(405, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
