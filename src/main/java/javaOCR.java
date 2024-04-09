/*
* Maftei Marius-Vasile
* 09.04.2024
* Proiect PIP
*/

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;
import java.util.List;

public class javaOCR
{
    // Function to perform OCR on all image files in a directory
    public void performOCRForDirectory(String directoryPath) {

        // Initialize DirectorySearch to list image files in the specified directory
        DirectorySearch directorySearch = new DirectorySearch(directoryPath);
        List<File> imageFiles = directorySearch.listImageFiles();

        // Initialize Tesseract OCR engine
        Tesseract fileToConvert = new Tesseract();

        fileToConvert.setDatapath("D:/Proiect-PIP/Tess4J/tessdata");
        fileToConvert.setLanguage("eng+ron");

        // Process each image file
        for (File imageFile : imageFiles) {
            try {
                // Perform OCR on the image file
                String ocrResult = fileToConvert.doOCR(imageFile);
                System.out.println("OCR Result for " + imageFile.getName() + ":\n" + ocrResult);
            } catch (TesseractException e) {
                System.err.println("Error performing OCR on file: " + imageFile.getName());
                e.printStackTrace();
            }
        }

        /*
    // Function to perform OCR on an image or PDF file
    public String performOCR(File file) throws TesseractException {
        Tesseract fileToConvert = new Tesseract();
        try {
            // the path of your tess data folder
            // inside the extracted file
            fileToConvert.setDatapath("D:/Proiect-PIP/Tess4J/tessdata");
            fileToConvert.setLanguage("eng");
            fileToConvert.setLanguage("ron");


            String text;
            text = fileToConvert.doOCR(new File("D:/Proiect-PIP/src/main/java/img_medicina.png"));

            return text;
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }
    */
    }

}
