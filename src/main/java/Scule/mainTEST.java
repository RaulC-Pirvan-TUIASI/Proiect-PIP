package Scule;

import OCR.javaOCR;

import java.io.File;

public class mainTEST {

    public static void main(String[] args) throws Exception
    {
        String calea = "H:\\Other computers\\My Laptop\\Javra\\PIPpr\\Proiect-PIP\\pozici\\grilaCapitale.png";
        javaOCR ocrProcessor = new javaOCR();
        System.out.println(ocrProcessor.performOCR(new File(calea)));
    }
}
