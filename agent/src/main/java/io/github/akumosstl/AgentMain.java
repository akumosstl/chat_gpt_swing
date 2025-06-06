package io.github.akumosstl;

import io.github.akumosstl.automation.Recorder;
import io.github.akumosstl.http.SimpleHttpServer;

import java.lang.instrument.Instrumentation;

public class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        try {
            System.out.println("[Agent] Starting...");

            Recorder recorder = new Recorder();
            SimpleHttpServer server = new SimpleHttpServer(recorder);
            server.start(8080);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("[Agent] Shutting down.");
                server.stop();
            }));

            Recorder.setupGlobalListeners();

            System.out.println("[Agent] Started successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
