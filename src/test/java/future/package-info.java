/**
 * This package contains classes related to file
 * searching and filtering functionalities.
 * <p>
 * The main class in this package is ocr/SearchImagesInDirectory
 * which provides methods to search and
 * list image files within a specified directory.
 * The package also includes test classes
 * to verify the functionality using JUnit.
 * </p>
 *
 * <p>Dependencies:</p>
 * <ul>
 *   <li>JUnit 5: For writing and running tests</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 * {@code
 * SearchImagesInDirectory searchImagesInDirectory;
 * searchImagesInDirectory = new SearchImagesInDirectory("path/to/directory");
 * List<File> imageFiles = searchImagesInDirectory.listImageFiles();
 * for (File file : imageFiles) {
 *     System.out.println(file.getName());
 * }
 * }
 * </pre>
 *
 * @since 1.0
 */
package future;
