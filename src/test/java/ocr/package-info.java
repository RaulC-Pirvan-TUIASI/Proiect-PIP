/**
 * This package contains classes related to
 * Optical Character Recognition (OCR) functionality.
 * <p>
 * The primary class in this package is ocr/JavaReadTextFromImage
 * which provides methods to perform OCR on images.
 * The package also includes tests to verify the OCR functionality using JUnit.
 * </p>
 *
 * <p>Dependencies:</p>
 * <ul>
 *   <li>tess4j: A Java wrapper for Tesseract OCR API</li>
 *   <li>junit.jupiter.api: JUnit 5 framework for testing</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 * {@code
 * JavaReadTextFromImage ocr = new JavaReadTextFromImage();
 * File file = new File("path/to/image.png");
 * try {
 *     String result = ocr.performocr(file);
 *     System.out.println(result);
 * } catch (TesseractException e) {
 *     e.printStackTrace();
 * }
 * }
 * </pre>
 *
 * @since 1.0
 * @author Maftei Marius-Vasile
 * @version 1.0
 */
package OCR;
