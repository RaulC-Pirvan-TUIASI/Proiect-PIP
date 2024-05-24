package future;

import ocr.JavaReadTextFromImage;

import java.io.File;

/**
 * The MainTEST class serves as an entry point
 * for testing the OCR (Optical Character Recognition)
 *
 * functionality provided by the {@link JavaReadTextFromImage} class.
 */
//final
public final class MainTEST {
    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private MainTEST() {
        // Private constructor to prevent instantiation
        throw new IllegalStateException("Utility class");
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    //final
    public static void main(final String[] args) throws Exception {
        // Path to the image file to be processed
        String calea = "pozici/grilaCapitale.png";

        //Create an instance of JavaReadTextFromImage class for OCR processing
        JavaReadTextFromImage ocrProcessor = new JavaReadTextFromImage();

        // Perform OCR on the specified image file and print the result
        System.out.println(ocrProcessor.performocr(new File(calea)));

    }
}
