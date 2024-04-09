import java.io.File;
import java.util.List;

public class mainTEST {

    public static void main(String[] args) {
        String directoryPath = "D:/Proiect-PIP/src/main/java/images";
        javaOCR ocrProcessor = new javaOCR();
        ocrProcessor.performOCRForDirectory(directoryPath);
    }
}
