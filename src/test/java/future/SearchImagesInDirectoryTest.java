package future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.Assert.assertFalse;

/**
 * The type Directory search test.
 */
class SearchImagesInDirectoryTest {

    /**
     * List image files.
     */
    @Test
    void listImageFiles() {
        // Provide a valid directory path for testing
        String directoryPath = "D:\\PIP_22.20_28.05\\testImages";
        SearchImagesInDirectory searchImagesInDIrectory;
        searchImagesInDIrectory = new SearchImagesInDirectory(directoryPath);

        List<File> imageFiles = searchImagesInDIrectory.listImageFiles();

        // Assert that the list is not null and not empty
        Assertions.assertNotNull(imageFiles);
        assertFalse(imageFiles.isEmpty());

        // Optionally, you can add more
        // assertions based on your requirements
        // For example, you can assert that all files
        // in the list are valid image files
        for (File file : imageFiles) {
            assertTrue(searchImagesInDIrectory.isImageFile(file));
        }
    }

    /**
     * Is image file.
     */
    @Test
    void isImageFile() {
        // Create a test instance of the SearchImagesInDirectory class
        SearchImagesInDirectory searchImagesInDIrectory;
        searchImagesInDIrectory = new SearchImagesInDirectory("");

        // Create sample files with various extensions
        File tifFile = new File("sample.tif");
        File jpegFile = new File("sample.jpeg");
        File jpgFile = new File("sample.jpg");
        File pngFile = new File("sample.png");
        File pdfFile = new File("sample.pdf");
        File txtFile = new File("sample.txt");

        // Assert that each file type is recognized as an image file
        assertTrue(searchImagesInDIrectory.isImageFile(tifFile));
        assertTrue(searchImagesInDIrectory.isImageFile(jpegFile));
        assertTrue(searchImagesInDIrectory.isImageFile(jpgFile));
        assertTrue(searchImagesInDIrectory.isImageFile(pngFile));
        assertTrue(searchImagesInDIrectory.isImageFile(pdfFile));

        // Assert that a non-image file is not recognized as an image file
        assertFalse(searchImagesInDIrectory.isImageFile(txtFile));
    }

}
