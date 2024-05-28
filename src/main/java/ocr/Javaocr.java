package ocr;
/*
 * Maftei Marius-Vasile
 * 09.04.2024
 * Proiect PIP
 */

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;

/**
 * The {@code javaOCR} class provides a method to perform OCR on image or PDF f
 * iles using the Tesseract library.
 */
public class Javaocr {

    /**
     * Default constructor for the {@code javaOCR} class.
     * This constructor is provided for documentation purposes and
     * to comply with the code quality guidelines.
     */
    public Javaocr() {
        // Default constructor
    }

    /**
     * Performs OCR on the specified image or PDF file.
     *
     * @param file the image or PDF file to perform OCR on
     * @return the extracted text from the file, or "eroare" if an error occurs
     * @throws TesseractException if an error occurs during OCR processing
     */
    public String performOCR(final File file) throws TesseractException {
        Tesseract fileToConvert = new Tesseract();
        try {
            // the path of your tessdata folder
            // inside the extracted file
            fileToConvert.setDatapath("Tess4J\\tessdata");
            //fileToConvert.setDatapath("H:\\Other computers\\My Laptop\\Javra
            // \\PIPpr\\Proiect-PIP\\Tess4J\\tessdata");
            fileToConvert.setLanguage("eng");
            fileToConvert.setLanguage("ron");

            String text = fileToConvert.doOCR(file);
            return text;
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        return "eroare";
    }
}
