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

    // Function to perform OCR on an image or PDF file*/
    public String performOCR(File file) throws TesseractException
    {
        Tesseract fileToConvert = new Tesseract();
        try {
            // the path of your tess data folder
            // inside the extracted file
            fileToConvert.setDatapath("H:\\Other computers\\My Laptop\\Javra\\PIPpr\\Proiect-PIP\\Tess4J\\tessdata");
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

