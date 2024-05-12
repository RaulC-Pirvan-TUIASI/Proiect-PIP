package Future;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectorySearch {
    private final String directoryPath;

    public DirectorySearch(String directoryPath){
        this.directoryPath = directoryPath;
    }

    // Function to list image files (.tif, .jpeg, .jpg, .png, .pdf) in a directory
    public List<File> listImageFiles() {
        List<File> imageFiles = new ArrayList<>();
        //validate the directory path
        File directory = new File(directoryPath);

        if(!directory.isDirectory()){
            System.err.println(directoryPath + "is not a valid directory path.");
            //Return empty list if directory is not valid
            return imageFiles;
        }

        //List all file from directory
        File[] files = directory.listFiles();

        if (files != null) {
            for(File file : files){
                if(isImageFile(file)){
                    imageFiles.add(file);
                }
            }
        }
        return imageFiles;
    }

    public boolean isImageFile(File file){
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".tif") || fileName.endsWith(".jpeg") ||
               fileName.endsWith(".jpg") || fileName.endsWith(".png") ||
               fileName.endsWith(".pdf");
    }
}
