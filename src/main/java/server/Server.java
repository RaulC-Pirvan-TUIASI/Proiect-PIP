package server;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

public class Server {
    private static final String WEB_ROOT = "src/main/resources";
    private static final String UPLOADED_PHOTOS_DIR = "photos/";

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
                String uri = exchange.getRequestURI().toString();  //what should happen here ???
                uri=uri.replace('/','\\');
                System.out.println(uri + " <<<ce avem noi aici");
                String filePath = WEB_ROOT + uri;
                //String filePath = WEB_ROOT + "\\index.html";
                File file = new File(filePath);
                if (file.exists() && !file.isDirectory()) {
                    System.out.println("Request for file: " + uri);
                    serveFile(exchange, file);
                } else {
                    System.out.println("File not found: " + uri);
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
        if(fileName.endsWith(".imp"))
        {
            System.out.println(fileName);
        }
        else if (fileName.endsWith(".html") || fileName.endsWith(".htm") || fileName.startsWith("index")) {
            System.out.println(fileName);
            contentType = "text/html";
        } else if (fileName.endsWith(".css")) {
            System.out.println("CSS");
            contentType = "text/css";
        } else if (fileName.endsWith(".js")) {
            contentType = "application/javascript";
        }
        return contentType;
    }

    static byte[] removeHeaders(byte[] data) {
        // Find the index where the header ends
        int headerEndIndex = findHeaderEndIndex(data);

        // If headerEndIndex is -1, indicating the header end sequence wasn't found, return the original data
        if (headerEndIndex == -1) {
            return data;
        }

        // Calculate the length of the content after the header
        int contentLength = data.length - (headerEndIndex + 4); // 4 represents the length of "\r\n\r\n"

        // Create a new byte array to hold the content after the header
        byte[] content = new byte[contentLength];

        // Copy the content after the header to the new byte array
        System.arraycopy(data, headerEndIndex + 4, content, 0, contentLength);

        return content;
    }

    static int findHeaderEndIndex(byte[] data) {
        // Iterate through the byte array looking for the "\r\n\r\n" sequence
        for (int i = 0; i < data.length - 3; i++) {
            if (data[i] == '\r' && data[i + 1] == '\n' && data[i + 2] == '\r' && data[i + 3] == '\n') {
                // Return the index where the sequence begins
                return i;
            }
        }
        // If the sequence isn't found, return -1
        return -1;
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
                System.out.println("Received data length: " + data.length );
                data=removeHeaders(data);


                // Generate a unique file name for the uploaded photo
                String filename = generateUniqueFileName("uploaded_photos", "png");
                //filename="teapa.png";

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
            return UPLOADED_PHOTOS_DIR + "photo_" + timestamp + "." + extension;
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
