/**
 * The Server class implements a simple HTTP server that serves static files and handles file uploads.
 * It includes functionality for handling GET requests to serve static files and POST requests to upload files.
 */
package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

import static aplicatie.Gpt.mainulet;

/**
 * The Server class implements a simple HTTP server that serves static files and handles file uploads.
 */
public class Server {

    /** The root directory for serving static files. */
    private static final String WEB_ROOT = "src/main/resources";
    /** The directory where uploaded photos are stored. */
    private static final String UPLOADED_PHOTOS_DIR = "pozici/";

    /**
     * The RootHandler class handles GET requests for serving static files.
     */
    static class RootHandler implements HttpHandler {
        /**
         * Handles HTTP GET requests by serving static files.
         *
         * @param exchange The HttpExchange object representing the client's request and response context.
         * @throws IOException If an I/O error occurs while serving the static file or writing the response.
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
                String uri = exchange.getRequestURI().toString();
                uri = uri.replace('/', '\\');
                System.out.println(uri + " <<<ce avem noi aici");
                String filePath = WEB_ROOT + uri;
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

    /**
     * Sends the specified file as a response to the client.
     *
     * @param exchange The HttpExchange object representing the client's request and response context.
     * @param file     The file to be sent to the client.
     * @throws IOException If an I/O error occurs while sending the file.
     */
    static void serveFile(HttpExchange exchange, File file) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", getContentType(file));
        exchange.sendResponseHeaders(200, file.length());
        try (OutputStream output = exchange.getResponseBody();
             InputStream input = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        System.out.println("File sent to client: " + file.getName());
    }

    /**
     * Sends a 404 (Not Found) response to the client.
     *
     * @param exchange The HttpExchange object representing the client's request and response context.
     * @throws IOException If an I/O error occurs while sending the response.
     */
    static void serve404(HttpExchange exchange) throws IOException {
        String response = "404 (Not Found)\n";
        exchange.sendResponseHeaders(404, response.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes());
        output.close();
        System.out.println("404 response sent to client");
    }

    /**
     * Determines the content type of the specified file based on its extension.
     *
     * @param file The file for which to determine the content type.
     * @return The content type of the file.
     */
    static String getContentType(File file) {
        String contentType = "text/plain";
        String fileName = file.getName();
        if (fileName.endsWith(".html") || fileName.endsWith(".htm") || fileName.startsWith("index")) {
            contentType = "text/html";
        } else if (fileName.endsWith(".css")) {
            contentType = "text/css";
        } else if (fileName.endsWith(".js")) {
            contentType = "application/javascript";
        }
        return contentType;
    }

    /**
     * The UploadHandler class handles POST requests for uploading files.
     */
    static class UploadHandler implements HttpHandler {
        /**
         * Handles HTTP POST requests by processing uploaded files.
         *
         * @param exchange The HttpExchange object representing the client's request and response context.
         * @throws IOException If an I/O error occurs while processing the uploaded file or sending the response.
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                // Get the headers and boundary
                Headers headers = exchange.getRequestHeaders();
                String contentType = headers.getFirst("Content-Type");
                String[] parts = contentType.split(";");
                for (String part : parts) {
                    if (part.trim().startsWith("boundary=")) {
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
                data = removeHeaders(data);

                // Generate a unique file name for the uploaded photo
                String filename = generateUniqueFileName();

                try {
                    // Write the received data to the specified file
                    Files.write(Paths.get(filename), data);

                    filename = filename.replace('/', '\\');
                    System.out.println("ianitn de DEBUG");

                    String omnigrila = mainulet(filename);
                    System.out.println("Photo saved successfully: " + filename);

                    // Send a custom response header with the message
                    exchange.getResponseHeaders().set("Raspunsul magic", "omnigrila");

                    // Send a basic response
                    byte[] responseData = omnigrila.getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(200, responseData.length);

                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(omnigrila.getBytes());
                    responseBody.close();

                    System.out.println("Upload response sent to client");
                } catch (IOException e) {
                    // Log any exceptions that occur during file writing
                    System.err.println("Error saving photo: " + e.getMessage());
                    e.printStackTrace();
                    serve500(exchange);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                serve404(exchange);
            }
        }

        /**
         * Generate a unique file name for the uploaded photo.
         *
         * @return The generated unique file name.
         */
        private String generateUniqueFileName() {
            String timestamp = Instant.now().toString().replace(":", "-");
            return UPLOADED_PHOTOS_DIR + "photo_" + timestamp + "." + "png";
        }
    }

    /**
     * Removes HTTP headers from the byte array.
     *
     * @param data The byte array containing HTTP headers and content.
     * @return The byte array with HTTP headers removed.
     */
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

        // Check if there is an ending header starting with 0D 0A 2D 2D
        int endHeaderIndex = findEndHeaderIndex(content);
        if (endHeaderIndex != -1) {
            // Trim the content array to exclude the ending header
            byte[] trimmedContent = new byte[endHeaderIndex];
            System.arraycopy(content, 0, trimmedContent, 0, endHeaderIndex);
            return trimmedContent;
        }

        return content;
    }

    /**
     * Finds the index where the HTTP header ends in the byte array.
     *
     * @param data The byte array containing HTTP headers and content.
     * @return The index where the HTTP header ends, or -1 if the end sequence wasn't found.
     */
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

    /**
     * Finds the index where the ending header starts in the byte array.
     *
     * @param data The byte array containing HTTP content.
     * @return The index where the ending header starts, or -1 if the sequence wasn't found.
     */
    static int findEndHeaderIndex(byte[] data) {
        // Iterate through the byte array looking for the end header sequence "0D 0A 2D 2D"
        for (int i = 0; i < data.length - 3; i++) {
            if (data[i] == 0x0D && data[i + 1] == 0x0A && data[i + 2] == 0x2D && data[i + 3] == 0x2D) {
                // Return the index where the sequence begins
                return i;
            }
        }
        // If the sequence isn't found, return -1
        return -1;
    }

    /**
     * Sends a 500 (Internal Server Error) response to the client.
     *
     * @param exchange The HttpExchange object representing the client's request and response context.
     * @throws IOException If an I/O error occurs while sending the response.
     */
    static void serve500(HttpExchange exchange) throws IOException {
        String response = "500 (Internal Server Error)\n";
        exchange.sendResponseHeaders(500, response.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes());
        output.close();
        System.out.println("500 response sent to client");
    }

    /**
     * The main method starts the HTTP server on the specified port and creates handlers for requests.
     *
     * @param args Command-line arguments.
     * @throws Exception If an error occurs while starting the server.
     */
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
