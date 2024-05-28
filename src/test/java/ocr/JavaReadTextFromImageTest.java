package ocr;

import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * The type Java ocr test.
 */
        final class JavaReadTextFromImageTest {

    /**
     * Default constructor for JavaReadTextFromImageTest class.
     * This constructor is provided for documentation purposes and
     * to comply with the code quality guidelines.
     */
    private JavaReadTextFromImageTest() {
        // Default constructor
    }

    /**
     * Test perform ocr.
     */
    @Test
    void testPerformOCR() {
        // Create an instance of the JavaReadTextFromImage class
        JavaReadTextFromImage ocr = new JavaReadTextFromImage();

        // Provide a sample file for testing
        // (replace "path/to/your/file" with the actual file path)
        File file = new File("D:\\Tarzan\\testImages\\grilaCapitale.png");

        try {
            // Call the performOCR method and get the result
            String result = ocr.performocr(file);

            // Assert that the result is not null or empty
            Assertions.assertNotNull(result);
            Assertions.assertFalse(result.isEmpty());

            // Optionally, you can add additional assertions
            // based on your requirements
            // For example, you can assert that the result
            // contains specific text that you expect
            Assertions.assertTrue(result.contains("""
                    |Care este capitala ltaliei ?
                    * Roma
                    * Milano
                    - Bucuresti
                    * Romania
                    """));
        } catch (TesseractException e) {
            // If an exception occurs during ocr, fail the test
            Assertions.fail("ocr process threw an exception: "
                    + e.getMessage());
        }
    }
}
