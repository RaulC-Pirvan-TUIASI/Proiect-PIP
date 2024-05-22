/**
 * This package contains classes and
 * functionalities related to server operations.
 * <p>
 * The main class in this package is {@link server/Server},
 * which provides various methods for server-related tasks
 * such as determining the content type of files.
 * The package also includes test classes
 * to verify the functionality using JUnit.
 * </p>
 *
 * <p>Dependencies:</p>
 * <ul>
 *   <li>JUnit 5: For writing and running tests</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 * {@code
 * // Example usage of the Server class to get content type of a file
 * File file = new File("example.html");
 * String contentType = Server.getContentType(file);
 * System.out.println(contentType); // Outputs: text/html
 * }
 * </pre>
 *
 * @since 1.0
 * @see server/Server
 * @see server/ServerTest
 */
package server;
