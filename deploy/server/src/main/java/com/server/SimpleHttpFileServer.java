
package com.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class SimpleHttpFileServer {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String directory = ".";

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            directory = args[1];
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("Serving " + new File(directory).getAbsolutePath() + " on http://localhost:" + port);

        server.createContext("/", new FileHandler(directory));
        server.setExecutor(null);
        server.start();
    }

    static class FileHandler implements HttpHandler {
        private final String directory;

        public FileHandler(String directory) {
            this.directory = directory;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String filePath = exchange.getRequestURI().getPath();
                if (filePath.equals("/")) filePath = "/agent-swing-recorder.jnlp";

                File file = new File(directory + filePath).getCanonicalFile();
                if (!file.getPath().startsWith(new File(directory).getCanonicalPath())) {
                    String response = "403 (Forbidden)
";
                    exchange.sendResponseHeaders(403, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (!file.isFile()) {
                    String response = "404 (Not Found)
";
                    exchange.sendResponseHeaders(404, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String mime = Files.probeContentType(file.toPath());
                if (file.getName().endsWith(".jnlp")) {
                    mime = "application/x-java-jnlp-file";
                } else if (file.getName().endsWith(".jar")) {
                    mime = "application/java-archive";
                } else if (mime == null) {
                    mime = "application/octet-stream";
                }

                exchange.getResponseHeaders().set("Content-Type", mime);
                exchange.sendResponseHeaders(200, file.length());

                OutputStream os = exchange.getResponseBody();
                FileInputStream fs = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int count;
                while ((count = fs.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
                fs.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
