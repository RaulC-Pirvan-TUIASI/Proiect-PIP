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
   * Perform ocr string.
   *
   * @param file the file
   * @return the string
   * @throws TesseractException the tesseract exception
   */
// Function to perform ocr on an image or PDF file*/
  public String performocr(File file) throws TesseractException {
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

