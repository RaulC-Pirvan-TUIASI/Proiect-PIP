

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * Hello world!
 *
 */
public class javaOCR
{
    javaOCR()
    {
        Tesseract tsr = new Tesseract();
        try {

            tsr.setDatapath("D:/Proiect-PIP/Tess4J/tessdata");
            tsr.setLanguage("eng");
            tsr.setLanguage("ron");

            // the path of your tess data folder
            // inside the extracted file
            String text;
            text = tsr.doOCR(new File("D:/Proiect-PIP/src/main/java/img_medicina.png"));

            // path of your image file
            System.out.print(text);
        }
        catch (TesseractException e) {
            e.printStackTrace();
        }
    }
    public static void main( String[] args ){

        javaOCR jOCR = new javaOCR();
  }
}
