package server;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

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
                    System.out.println("Request for file: " + filePath);
                    serveFile(exchange, file);
                } else {
                    System.out.println("File not found: " + filePath);
                    serve404(exchange);
                }
            } else {
                System.out.println("Unsupported request method: " + requestMethod);
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
        System.out.println("File sent to client: " + file.getName());
    }

    static void serve404(HttpExchange exchange) throws IOException {
        String response = "404 (Not Found)\n";
        exchange.sendResponseHeaders(404, response.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes());
        output.close();
        System.out.println("404 response sent to client");
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

    static class UploadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                // Get the headers and boundary
                Headers headers = exchange.getRequestHeaders();
                String contentType = headers.getFirst("Content-Type");
                String[] parts = contentType.split(";");
                String boundary = "";
                for (String part : parts) {
                    if (part.trim().startsWith("boundary=")) {
                        boundary = part.split("=")[1];
                        break;
                    }
                }

                // Create input stream to read the request body
                InputStream inputStream = exchange.getRequestBody();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                // Read the request body until the boundary is reached
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // Convert the output stream to byte array
                byte[] data = outputStream.toByteArray();
                System.out.println("Received data length: " + data.length);

                // Generate a unique file name for the uploaded photo
                String filename = generateUniqueFileName("uploaded_photos", "png");

                try {
                    // Write the received data to the specified file
                    Files.write(Paths.get(filename), data);
                    System.out.println("Photo saved successfully: " + filename);

                    // Send response to indicate successful upload
                    String response = "Photo uploaded successfully";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(response.getBytes());
                    responseBody.close();
                    System.out.println("Upload response sent to client");
                } catch (IOException e) {
                    // Log any exceptions that occur during file writing
                    System.err.println("Error saving photo: " + e.getMessage());
                    serve500(exchange);
                }
            } else {
                serve404(exchange);
            }
        }

        // Generate a unique file name for the uploaded photo
        private String generateUniqueFileName(String directory, String extension) {
            String timestamp = Instant.now().toString().replace(":", "-");
            return directory + File.separator + "photo_" + timestamp + "." + extension;
        }
    }

    // Method to handle internal server errors
    static void serve500(HttpExchange exchange) throws IOException {
        String response = "500 (Internal Server Error)\n";
        exchange.sendResponseHeaders(500, response.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes());
        output.close();
        System.out.println("500 response sent to client");
    }

    public static void main(String[] args) throws Exception {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RootHandler());
        server.createContext("/upload", new UploadHandler());
        server.setExecutor(null);
        System.out.println("Server is running on port " + port);
        server.start();
    }
}
