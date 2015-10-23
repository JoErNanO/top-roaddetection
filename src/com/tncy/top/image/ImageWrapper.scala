package com.tncy.top.image

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.IOException;

/** 
 * == Image Wrapper ==
 *
 * The [[com.tncy.top.image.ImageWrapper]] represents a wrapper class for java image objects.
 *  
 * The image is represented as a 2-D array representation, where each
 * pixel at (column,row) contains the RGB colour value for that pixel. The value
 * is represented as a 32-bit Integer value, taking the form:
 * 
 * <pre>
 *    (MSB) TTTT TTTT RRRR RRRR GGGG GGGG BBBB BBBB (LSB)
 * </pre>
 * 
 * where <code>MSB</code> is the most significant bit, <code>LSB</code> the least significant bit, T/R/G/B 
 * are 8-bit representations of the Transparency, Red, Green, and Blue colour values
 * respectively.
 *  
 * For example, an RGB value of <code>0x000000FF</code> (hexadecimal base) corresponds to colour blue:
 * 
 * {{{
 *   (MSB) TTTT TTTT RRRR RRRR GGGG GGGG BBBB BBBB (LSB)
 *         0000 0000 0000 0000 0000 0000 1111 1111
 * }}} 
 * 
 * === Example Use ===
 * 
 * {{{ 
 *    // Source image file
 *    var fileName : String = "sampleImage.jpg";
 *    
 *    // Load wrapped image
 *    var wrappedImage : ImageWrapper = new ImageWrapper(fileName);
 *    // Get the image
 *    var image2D : Array[Array[Int]] = wrappedImage.getImage();
 *    
 *    // Modify it
 *    for (row <- 0 to 40) {
 *      for (col <-0 to 80) {
 *        image2D(row)(col) = 0x000000FF; // Set these pixels to RGB blue
 *      }
 *    }
 *    
 *    // Destination image file
 *    var outputFile : String = "outputImage.jpg";
 *    // Save the result
 *    wrappedImage.saveImage(outputFile);
 * }}}
 * 
 * @author Francesco Giovannini
 */
class ImageWrapper(fileName : String) {
  
  /** The image height. */
  var height : Int = 0;
  /** The image width. */
  var width : Int = 0;
  
  /** The 2-D array of pixel RGB values  representing the image. */
  private var theImage2D : Array[Array[Int]] = loadImage(fileName);
  
  /** The 1-D array of pixel RGB values  representing the image. */
  private var theImage1D : Array[Int] = null;
  
   
  /** Get the wrapped image.
   *  
   *  @return A 2-D array of pixel RGB values  representing the image
   *  
   *  @author Francesco Giovannini
   */
  def getImage() : Array[Array[Int]] = {
    return this.theImage2D;
  }
  
  /** Get the wrapped image.
   *  
   *  @returns A 2-D array of pixel RGB values  representing the image
   *  
   *  @author Francesco Giovannini
   */
  private def getImage1D() : Array[Int] = {
    return this.theImage1D;
  }
  
  
  /** Save an image to file.
   *  
   * The image is represented as a 2-D array representation, where each
   * pixel at (column,row) contains the RGB colour value for that pixel. The value
   * is represented as a 32-bit Integer value, taking the form:
   * 
   * <pre>
   *    (MSB) TTTT TTTT RRRR RRRR GGGG GGGG BBBB BBBB (LSB)
   * </pre>
   * 
   * where <code>MSB</code> is the most significant bit, <code>LSB</code> the least significant bit, T/R/G/B 
   * are 8-bit representations of the Transparency, Red, Green, and Blue colour values
   * respectively.
   * 
   * The image file name must contain the file type extension for the image to be saved correctly:
   * 
   * {{{
   * var fileNameWRONG : String = "myTestImage";
   * var fileName : String = "myTestImage.png";
   * }}}
   * 
   *  @param fileName The path where to save the image file
   *  @param image A 2-D array of pixel RGB values representing the image
   *  @return True upon completion
   *  @throws IOException if the image cannot be found at the specified path
   *  @throws NullPointerException if the given filename is null
   *  
   *  @author Francesco Giovannini
   */
  def saveImage(fileName : String) : Boolean = {
    // Get image width and height
    val height = this.height;
    var width = this.width;;
    
    // Flatten the array
    this.theImage1D = this.theImage2D.flatten;
       
    // Create temporary BufferedImage
    var buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    buffImg.setRGB(0, 0, width, height, this.theImage1D, 0, width);
    
    // Save image to file
    try {
      var outputFile = new File(fileName);
      ImageIO.write(buffImg, fileName.takeRight(3), outputFile);
    } catch {
      case ioe : IOException => {
        println("Cannot save image to file " + fileName + ". Please check this path.");
        ioe.printStackTrace();
      }
      case nulle : NullPointerException => {
        println("The specified image file name is Null.");
        nulle.printStackTrace();
      }
    }

    //showImage(buffImg, "Converted Image");
    
    return true;
  }
  
  
  /** Convert the  2-D array of pixel RGB values representing the image into a pretty printable
   *  string.
   *  
   *  The array printout is formatted as:
   *  
   *  {{{
   *  	[ [1, 2, 3],
   *  	  [3, 4, 5],
   *  	  [6, 7, 8] ]
   *  }}}
   *  
   *  @author Francesco Giovannini
   */
  override def toString() = {
    val row : Array[String] = for (l : Array[Int] <- this.theImage2D) yield l.mkString("[ ", ", ", " ]")
    row.mkString("[ ",",\n"," ]")
  }
  
  
  /** Load an image from file.
   *  
   *  The image is represented as a 2-D array representation, where each
   *  pixel at (column,row) contains the RGB colour value for that pixel. The value
   *  is represented as a 32-bit Integer value, taking the form:
   * 
   *  <pre>
   *     (MSB) TTTT TTTT RRRR RRRR GGGG GGGG BBBB BBBB (LSB)
   *  </pre>
   * 
   *  where <code>MSB</code> is the most significant bit, <code>LSB</code> the least significant bit, T/R/G/B 
   *  are 8-bit representations of the Transparency, Red, Green, and Blue colour values
   *  respectively.
   *  
   *  The filename must be given as a full path to the file, absolute or relative.
   *  The function throws an error if the file cannot be found.
   *  
   *  @param fileName The path to the image file
   *  @return A 2-D array of pixel RGB values  representing the image
   *  @throws IOException if the image cannot be found at the specified path
   *  @throws NullPointerException if the given filename is null
   *  
   *  @author Francesco Giovannini
   */
  private def loadImage(fileName : String) : Array[Array[Int]] = {
    var loadedImage = null : BufferedImage;
    
    try {
      // Load image from path
      val sourceimage = new File(fileName) : File;
      loadedImage = ImageIO.read(sourceimage);
    } catch {
      case ioe : IOException => {
        println("The specified image file " + fileName + " cannot be found. Please check its path.");
        ioe.printStackTrace();
      }
      case nulle : NullPointerException => {
        println("The specified image file name is Null.");
        nulle.printStackTrace();
      }
    }
    
    // Save image information
    this.width = loadedImage.getWidth();
    this.height = loadedImage.getHeight();
    
    // Convert Java Image into a two-dimensional array and save it
    this.theImage2D = convertTo2DWithoutUsingGetRGB(loadedImage);
    
    return this.theImage2D;
  }
  
  
  /** Efficiently convert a <code>BufferedImage</code> to a 2-D array of pixels.
   *  
   *  The complexity of this is still O(Width*Height), however the conversion from RGB
   *  value is more efficient, thus the code execution is faster than looping through
   *  each pixel and calling BufferedImage.getRGB() on them.
   *   
   *  This code was taken from: http://stackoverflow.com/a/9470843 and then adapted
   *  to the scala syntax.
   *
   *  @param image The BufferedImage object to be converted
   *  @return A 2-D array of pixel RGB values representing the image
   *  
   *  @author Motasim  
   *  @author Francesco Giovannini
   */
  private def convertTo2DWithoutUsingGetRGB(image : BufferedImage) : Array[Array[Int]] = {

      val pixels = image.getRaster().getDataBuffer().asInstanceOf[DataBufferByte].getData() : Array[Byte];
      val width = image.getWidth() : Int;
      val height = image.getHeight() : Int;
      val hasAlphaChannel = image.getAlphaRaster() != null : Boolean;

      var result = Array.ofDim[Int](height, width) : Array[Array[Int]];
      if (hasAlphaChannel) { // The RGB values contain a transparency representation
         val pixelLength = 4 : Int;
         var col = 0 : Int;
         var row = 0 : Int;
         for (pixel : Int <- 0 to pixels.length - 1 by pixelLength) {
            var argb = 0 : Int;
            argb += ((pixels(pixel).asInstanceOf[Int] & 0xff) << 24); // alpha
            argb += (pixels(pixel + 1).asInstanceOf[Int] & 0xff); // blue
            argb += ((pixels(pixel + 2).asInstanceOf[Int] & 0xff) << 8); // green
            argb += ((pixels(pixel + 3).asInstanceOf[Int] & 0xff) << 16); // red
            result(row)(col) = argb;
            col += 1;
            if (col == width) {
               col = 0;
               row += 1;
            }
         }
      } else { // The RGB values don't contain a transparency representation
         val pixelLength = 3 : Int;
         var col = 0 : Int;
         var row = 0 : Int;
         for (pixel <- 0 to pixels.length - 1 by pixelLength) {
            var argb = 0 : Int;
            argb += -16777216; // 255 alpha
            argb += (pixels(pixel).asInstanceOf[Int] & 0xff); // blue
            argb += ((pixels(pixel + 1).asInstanceOf[Int] & 0xff) << 8); // green
            argb += ((pixels(pixel + 2).asInstanceOf[Int] & 0xff) << 16); // red
            result(row)(col) = argb;
            col += 1;
            if (col == width) {
               col = 0;
               row += 1;
            }
         }
      }

      return result;
   }
}