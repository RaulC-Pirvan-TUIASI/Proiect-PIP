package Future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;



/**
 * The type Directory search test.
 */
final class DirectorySearchTest {

    /**
     * Default constructor for DirectorySearchTest.
     * This constructor is provided for documentation purposes and
     * to comply with the code quality guidelines.
     */
    private DirectorySearchTest() {
        // Default constructor
    }

    /**
     * List image files.
     */
    @Test
    void listImageFiles() {
        // Provide a valid directory path for testing
        String directoryPath = "D:\\Tarzan\\testImages";
        DirectorySearch directorySearch = new DirectorySearch(directoryPath);

        List<File> imageFiles = directorySearch.listImageFiles();

        // Assert that the list is not null and not empty
        Assertions.assertNotNull(imageFiles);
        Assertions.assertFalse(imageFiles.isEmpty());

        // Optionally, you can add more assertions based on your requirements
        // For example, you can assert that all
        // files in the list are valid image files
        for (File file : imageFiles) {
            Assertions.assertTrue(directorySearch.isImageFile(file));
        }
    }

    /**
     * Is image file.
     */
    @Test
    void isImageFile() {
        // Create a test instance of the DirectorySearch class
        DirectorySearch directorySearch = new DirectorySearch("");

        // Create sample files with various extensions
        File tifFile = new File("sample.tif");
        File jpegFile = new File("sample.jpeg");
        File jpgFile = new File("sample.jpg");
        File pngFile = new File("sample.png");
        File pdfFile = new File("sample.pdf");
        File txtFile = new File("sample.txt");

        // Assert that each file type is recognized as an image file
        Assertions.assertTrue(directorySearch.isImageFile(tifFile));
        Assertions.assertTrue(directorySearch.isImageFile(jpegFile));
        Assertions.assertTrue(directorySearch.isImageFile(jpgFile));
        Assertions.assertTrue(directorySearch.isImageFile(pngFile));
        Assertions.assertTrue(directorySearch.isImageFile(pdfFile));

        // Assert that a non-image file is not recognized as an image file

        Assertions.assertFalse(directorySearch.isImageFile(txtFile));
    }
}
