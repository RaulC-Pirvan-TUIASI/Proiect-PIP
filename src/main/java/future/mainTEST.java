package future;

import ocr.JavaReadTextFromImage;

import java.io.File;

public class mainTEST {

    public static void main(String[] args) throws Exception
    {
        String calea = "H:\\Other computers\\My Laptop\\Javra\\PIPpr\\Proiect-PIP\\pozici\\grilaCapitale.png";
        JavaReadTextFromImage ocrProcessor = new JavaReadTextFromImage();
        System.out.println(ocrProcessor.performocr(new File(calea)));

    }
}
