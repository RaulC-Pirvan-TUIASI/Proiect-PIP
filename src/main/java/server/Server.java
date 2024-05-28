/**
 * The Server class implements a simple HTTP server
 * that serves static files and handles file uploads.
 * It includes functionality for handling GET requests to serve
 * static files and POST requests to upload files.
 */
package server;

    import com.sun.net.httpserver.Headers;
    import com.sun.net.httpserver.HttpExchange;
    import com.sun.net.httpserver.HttpHandler;
    import com.sun.net.httpserver.HttpServer;
    import java.io.BufferedInputStream;
    import java.io.ByteArrayOutputStream;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;
    import java.net.InetSocketAddress;
    import java.nio.charset.StandardCharsets;
    import java.nio.file.Files;
    import java.nio.file.Paths;
    import java.time.Instant;

    import static aplicatie.Gpt.mainulet;
  /*
  *  import com.sun.net.httpserver.*;
  *  import java.io.*;
  *  import java.net.InetSocketAddress;
  *  import java.nio.charset.StandardCharsets;
  *  import java.nio.file.Files;
  *  import java.nio.file.Paths;
  *  import java.time.Instant;
  *
  *  import static aplicatie.Gpt.mainulet;
  */

/**
 * The Server class implements a simple HTTP server
 * that serves static files and handles file uploads.
 * This is a utility class and should not be instantiated
 */
//finala
public final class  Server {

    /** The root directory for serving static files. */
    private static final String WEB_ROOT = "src/main/resources";
    /** The directory where uploaded photos are stored. */
    private static final String UPLOADED_PHOTOS_DIR = "pozici/";

    // Define constants for HTTP status codes
    /**
     * HTTP status code for OK (200).
     */
    private static final int HTTP_OK = 200;

    /**
     * HTTP status code for Not Found (404).
     */
    private static final int HTTP_NOT_FOUND = 404;

    /**
     * HTTP status code for Internal Server Error (500).
     */
    private static final int HTTP_INTERNAL_ERROR = 500;

    /**
     * The buffer size used for reading and writing data (4096 bytes).
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * The length of the HTTP header end sequence "\r\n\r\n" (4 bytes).
     */
    private static final int HEADER_END_LENGTH = 4;

    /**
     * The length of the boundary end sequence "--\r\n" (3 bytes).
     */
    private static final int BOUNDARY_END_LENGTH = 3;

    /**
     * The carriage return character (0x0D).
     */
    private static final byte CR = 0x0D;

    /**
     * The line feed character (0x0A).
     */
    private static final byte LF = 0x0A;

    /**
     * The hyphen character (0x2D).
     */
    private static final byte HYPHEN = 0x2D;

    /**
     * The port number for the HTTP server (8000).
     */
    private static final int PORT = 8000;

    // Private constructor to prevent instantiation
    private Server() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    /**
     * The RootHandler class handles GET requests for serving static files.
     */
    static class RootHandler implements HttpHandler {
        /**
         * Handles HTTP GET requests by serving static files.
         *
         * @param exchange The HttpExchange object
         * representing the client's request and response context.
         *
         * @throws IOException If an I/O error occurs
         * while serving the static file or writing the response.
         */
        //modificat HttpExchange  in final
        @Override
        public void handle(final HttpExchange exchange) throws IOException {
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
                System.out.println("Unsupported request method: "
                        + requestMethod);
                serve404(exchange);
            }
        }
    }

    /**
     * Sends the specified file as a response to the client.
     *
     * @param exchange The HttpExchange object
     * representing the client's request and response context.
     *
     * @param file The file to be sent to the client.
     *
     * @throws IOException If an I/O error occurs
     * while sending the file.
     */
//schimbat in final, si file tot final
    static void serveFile(final HttpExchange exchange, final File file)
            throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", getContentType(file));
        exchange.sendResponseHeaders(HTTP_OK, file.length());
        try (OutputStream output = exchange.getResponseBody();
             InputStream input = new BufferedInputStream(
                     new FileInputStream(file))) {
            byte[] buffer = new byte[BUFFER_SIZE];
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
     * @param exchange The HttpExchange object
     * representing the client's request and response context.
     *
     * @throws IOException If an I/O error occurs while sending the response.
     */
//schimbat in final
    static void serve404(final HttpExchange exchange) throws IOException {
        String response = "404 (Not Found)\n";
        exchange.sendResponseHeaders(
                HTTP_NOT_FOUND, response.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes());
        output.close();
        System.out.println("404 response sent to client");
    }

    /**
     * Determines the content type of the specified file based on its extension.
     *
     * @param file The file for which to determine the content type.
     *
     * @return The content type of the file.
     */
//file in final
    static String getContentType(final File file) {
        String contentType = "text/plain";
        String fileName = file.getName();
        if (fileName.endsWith(".html")
                || fileName.endsWith(".htm")
                || fileName.startsWith("index")) {
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
         * @param exchange The HttpExchange object representing
         * the client's request and response context.
         *
         * @throws IOException If an I/O error occurs
         * while processing the uploaded file or sending the response.
         */
        //final
        @Override
        public void handle(final HttpExchange exchange) throws IOException {
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
                ByteArrayOutputStream outputStream;
                outputStream = new ByteArrayOutputStream();
                // Read the request body until the boundary is reached
                byte[] buffer = new byte[BUFFER_SIZE];
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
                    exchange.getResponseHeaders()
                            .set("Raspunsul magic", "omnigrila");

                    // Send a basic response
                    byte[] responseData
                            = omnigrila.getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(HTTP_OK, responseData.length);

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
     *
     * @return The byte array with HTTP headers removed.
     */
//final byte
    static byte[] removeHeaders(final byte[] data) {
        // Find the index where the header ends
        int headerEndIndex = findHeaderEndIndex(data);

        // If headerEndIndex is -1,
        // indicating the header end sequence wasn't found,
        // return the original data
        if (headerEndIndex == -1) {
            return data;
        }

        // Calculate the length of the content after the header
        // 4 represents the length of "\r\n\r\n"
        int contentLength = data.length - (headerEndIndex + HEADER_END_LENGTH);

        // Create a new byte array to hold the content after the header
        byte[] content = new byte[contentLength];

        // Copy the content after the header to the new byte array
        System.arraycopy(data,
                headerEndIndex + HEADER_END_LENGTH,
                content, 0, contentLength);

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
     *
     * @return The index where the HTTP header ends,
     * or -1 if the end sequence wasn't found.
     */
//byte final
    static int findHeaderEndIndex(final byte[] data) {
        // Iterate through the byte array looking for the "\r\n\r\n" sequence
        for (int i = 0; i < data.length - BOUNDARY_END_LENGTH; i++) {
            if (data[i] == '\r'
                    && data[i + 1] == '\n'
                    && data[i + 2] == '\r'
                    && data[i + BOUNDARY_END_LENGTH] == '\n') {
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
     *
     * @return The index where the ending header starts,
     * or -1 if the sequence wasn't found.
     */
    //final byte
    static int findEndHeaderIndex(final byte[] data) {
        // Iterate through the byte array looking for
        // the end header sequence "0D 0A 2D 2D"
        for (int i = 0; i < data.length - BOUNDARY_END_LENGTH; i++) {
            if (data[i] == CR
                    && data[i + 1] == LF
                    && data[i + 2] == HYPHEN
                    && data[i + BOUNDARY_END_LENGTH] == HYPHEN) {
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
     * @param exchange The HttpExchange object representing
     * the client's request and response context.
     *
     * @throws IOException If an I/O error occurs while sending the response.
     */
//final si aici
    static void serve500(final HttpExchange exchange)
            throws IOException {
        String response = "500 (Internal Server Error)\n";
        exchange.sendResponseHeaders(
                HTTP_INTERNAL_ERROR, response.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(response.getBytes());
        output.close();
        System.out.println("500 response sent to client");
    }

    /**
     * The main method starts the HTTP server
     * on the specified port and creates handlers for requests.
     *
     * @param args Command-line arguments.
     *
     * @throws Exception If an error occurs while starting the server.
     */
//final si aici
    public static void main(final String[] args)
            throws Exception {
        int port = PORT;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RootHandler());
        server.createContext("/upload", new UploadHandler());
        server.setExecutor(null);
        System.out.println("Server is running on port " + port);
        server.start();
    }
}
