package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
                String uri = exchange.getRequestURI().toString();
                String filePath = "./src/main/resources" + uri;
                File file = new File(filePath);
                if (file.exists() && !file.isDirectory()) {
                    serveFile(exchange, file);
                } else {
                    serve404(exchange);
                }
            } else {
                serve404(exchange);
            }
        }
    }

    static void serveFile(HttpExchange exchange, File file) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", getContentType(file));
        exchange.sendResponseHeaders(200, file.length());
        OutputStream output = exchange.getResponseBody();
        Files.copy(file.toPath(), output);
        output.close();
    }

    static void serve404(HttpExchange exchange) throws IOException {
        String response = "404 (Not Found)\n";
        exchange.sendResponseHeaders(404, response.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes());
        output.close();
    }

    static String getContentType(File file) {
        String contentType = "text/plain";
        String fileName = file.getName();
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            contentType = "text/html";
        } else if (fileName.endsWith(".css")) {
            contentType = "text/css";
        } else if (fileName.endsWith(".js")) {
            contentType = "text/javascript";
        }
        return contentType;
    }

    public static void main(String[] args) throws Exception {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RootHandler());
        server.setExecutor(null);
        System.out.println("Server is running on port " + port);
        server.start();
    }
}
