import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class server {
    static String baseFolder = "H:\\Other computers\\My Laptop\\Javra\\PIP_proiect\\src\\main\\resources";
    static HttpServer server;

    public static void main(String[] args) throws IOException {
        int port = 8089; // You can change this to any port you prefer
        server = HttpServer.create(new java.net.InetSocketAddress(port), 0);
        server.createContext("/", new MainHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server is listening on port " + port);

        // Hook to shutdown the server gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.stop(0);
            System.out.println("Server stopped");
        }));
    }

    static class MainHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath();
            switch (path) {
                case "/":
                case "/main.html":
                    serveFile(t, "main.html");
                    break;
                case "/styles.css":
                    serveFile(t, "styles.css");
                    break;
                case "/script.js":
                    serveFile(t, "script.js");
                    break;
                case "/upload":
                    handleFileUpload(t);
                    break;
                default:
                    sendResponse(t, 404, "Not Found");
            }
        }

        private void serveFile(HttpExchange t, String fileName) throws IOException {
            File file = new File(baseFolder, fileName);
            if (file.exists()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                t.sendResponseHeaders(200, bytes.length);
                OutputStream os = t.getResponseBody();
                os.write(bytes, 0, bytes.length);
                os.close();
            } else {
                sendResponse(t, 404, "File not found: " + fileName);
            }
        }

        private void handleFileUpload(HttpExchange t) throws IOException {
            // Get uploaded file from request
            InputStream inputStream = t.getRequestBody();
            // Handle file upload as needed
            // For example, you can save the uploaded file to a directory
            // File uploadDirectory = new File("path/to/upload/directory");
            // ...
            sendResponse(t, 200, "File uploaded successfully");
        }

        private void sendResponse(HttpExchange t, int statusCode, String response) throws IOException {
            t.sendResponseHeaders(statusCode, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
