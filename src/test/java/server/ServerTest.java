package server;

import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains tests for the Server class,
 * specifically for the getContentType method.
 */
public class ServerTest {

    /**
     * Default constructor for the ServerTest class.
     * This constructor is provided for
     * documentation purposes.
     */
    public ServerTest() {
        // Default constructor
    }

    /**
     * Tests the getContentType method of
     * the Server class with various
     * file types.
     */
    @Test
    void testGetContentType() {
        // Test for HTML file
        File htmlFile = new File("index.html");
        String htmlContentType = Server.getContentType(htmlFile);
        assertEquals("text/html", htmlContentType);

        // Test for CSS file
        File cssFile = new File("styles.css");
        String cssContentType = Server.getContentType(cssFile);
        assertEquals("text/css", cssContentType);

        // Test for JavaScript file
        File jsFile = new File("script.js");
        String jsContentType = Server.getContentType(jsFile);
        assertEquals("application/javascript", jsContentType);

        // Test for other file types
        File txtFile = new File("document.txt");
        String txtContentType = Server.getContentType(txtFile);
        assertEquals("text/plain", txtContentType);

        // Test for unknown file extension
        File unknownFile = new File("file.unknown");
        String unknownContentType = Server.getContentType(unknownFile);
        assertEquals("text/plain", unknownContentType);
    }
}
