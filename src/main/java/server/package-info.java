/**
 * The {@code server} package provides a simple HTTP server
 * that serves static files and handles file uploads.
 *
 * <p>This package includes the following classes:</p>
 * <ul>
 *   <li> server.Server - The main class that starts the HTTP server
 *   and sets up request handlers.</li>
 *   <li> server.Server.RootHandler - Handles HTTP GET requests to serve
 *   static files from the specified web root directory.</li>
 *   <li> server.Server.UploadHandler - Handles HTTP POST requests
 *   to process and save uploaded files.</li>
 * </ul>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Serves static files such as HTML, CSS,
 *   and JavaScript from the {@code src/main/resources} directory.</li>
 *   <li>Processes file uploads and saves them
 *   to the {@code pozici/} directory with a unique filename.</li>
 *   <li>Sends appropriate HTTP responses, including
 *   404 (Not Found) and
 *   500 (Internal Server Error)
 *   when necessary.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * public static void main(String[] args) throws Exception {
 *     int port = 8000;
 *     HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
 *     server.createContext("/", new Server.RootHandler());
 *     server.createContext("/upload", new Server.UploadHandler());
 *     server.setExecutor(null);
 *     System.out.println("Server is running on port " + port);
 *     server.start();
 * }
 * }
 * </pre>
 */
package server;