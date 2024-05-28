package ocr;

/*
 * Maftei Marius-Vasile
 * 09.04.2024
 * Proiect PIP
 */

import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * The type Java ocr.
 */
public class JavaReadTextFromImage {


  /**
   * Default constructor for JavaReadTextFromImage class.
   * This constructor is provided for documentation purposes and
   * to comply with the code quality guidelines.
   */
  public JavaReadTextFromImage() {
    // Default constructor
  }

  /**
   * Performs OCR (Optical Character Recognition) on an image or PDF file.
   *
   * @param file the image or PDF file to perform OCR on
   * @return the extracted text from the file, or "eroare" if an error occurs
   * @throws TesseractException if an error occurs during OCR processing
   */
  public String performocr(final File file) throws TesseractException {


    Tesseract fileToConvert = new Tesseract();
    try {
      // the path of your tess data folde
      // inside the extracted file
      fileToConvert.setDatapath("Tess4J\\tessdata");
      fileToConvert.setLanguage("eng");
      fileToConvert.setLanguage("ron");

      String text;

      text = fileToConvert.doOCR(file);

      return text;
    } catch (TesseractException e) {
      e.printStackTrace();
    }

    return "eroare";
  }
}
