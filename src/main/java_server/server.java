import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;


public class server {

    static String baseFolder = "H:\\Other computers\\My Laptop\\Javra\\PIP_proiect\\src\\main\\resources";
    static HttpServer server;

    public static String getPWD()
    {
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current directory: " + currentDirectory);

        return currentDirectory;
    }

    public static void main(String[] args) throws IOException {
        baseFolder=getPWD();
        baseFolder=baseFolder+"\\src\\main\\resources";
        int port = 8090; // You can change this to any port you prefer
        server = HttpServer.create(new java.net.InetSocketAddress(port), 0);
        server.createContext("/", new MainHandler());
        server.createContext("/upload", new UploadHandler());
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

        private void sendResponse(HttpExchange t, int statusCode, String response) throws IOException {
            t.sendResponseHeaders(statusCode, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class UploadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Handle file upload
            handleFileUpload(exchange);
        }

        private void handleFileUpload(HttpExchange exchange) throws IOException {
            Headers headers = exchange.getRequestHeaders();
            String fileName = extractFileNameFromHeaders(headers);
            if (fileName != null) {
                Path filePath = Paths.get(baseFolder, fileName);
                try (InputStream inputStream = exchange.getRequestBody()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                System.out.println("File uploaded and saved to: " + filePath);
                sendResponse(exchange, 200, "File uploaded successfully");
            } else {
                sendResponse(exchange, 400, "Bad Request");
            }
        }

        private String extractFileNameFromHeaders(Headers headers) {
            List<String> contentDisposition = headers.get("Content-Disposition");
            if (contentDisposition != null && !contentDisposition.isEmpty()) {
                String line = contentDisposition.get(0);
                int startIndex = line.indexOf("filename=");
                if (startIndex != -1) {
                    startIndex += "filename=".length();
                    int endIndex = line.indexOf(';', startIndex);
                    if (endIndex == -1) {
                        endIndex = line.length();
                    }
                    return line.substring(startIndex, endIndex).trim().replace("\"", "");
                }
            }
            return null;
        }


        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.sendResponseHeaders(statusCode, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
