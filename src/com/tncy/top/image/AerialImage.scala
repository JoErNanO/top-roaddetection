package com.tncy.top.image

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.FlowLayout; 


object AerialImage {
  
  /** Load an image from file.
   *  
   *  The loaded image is converted into a 2-D array representation, where each
   *  pixel at (column,row) contains the RGB colour value for that pixel. The value
   *  is represented as a 32-bit Integer value, taking the form:
   *  	(MSB) TTTT TTTT RRRR RRRR GGGG GGGG BBBB BBBB (LSB)
   *  where MSB is the most significant bit, LSB the least significant bit, T/R/G/B 
   *  are 8-bit representations of the Transparency, Red, Green, and Blue colour values
   *  respectively.
   *  
   *  The filename must be given as a full path to the file, absolute or relative.
   *  The function throws an error if the file cannot be found.
   *  
   *  @param fileName The path to the image file
   *  @returns A 2-D array of pixel RGB values  representing the image
   *  @throws IOException if the image cannot be found at the specified path
   *  @throws NullPointerException if the given filename is null
   *  
   *  @author Francesco Giovannini
   */
  def loadImage(fileName : String) : Array[Array[Int]] = {
    var loadedImage = null : BufferedImage;
    
    try {
      // Load image from path
      val sourceimage = new File(fileName) : File;
      loadedImage = ImageIO.read(sourceimage);
    } catch {
      case ioe : IOException => {
        println("The specified image file " + fileName + "cannot be found. Please check its path.");
        ioe.printStackTrace();
      }
      case nulle : NullPointerException => {
        println("The specified image file name is Null.");
        nulle.printStackTrace();
      }
    }
    
    // Convert Java Image into a two-dimensional array
    var image = convertTo2DWithoutUsingGetRGB(loadedImage) : Array[Array[Int]];
    return image;
  }
  
  /** Save an image to file.
   *  
   *  The image is is represented as a 2-D array representation, where each
   *  pixel at (column,row) contains the RGB colour value for that pixel. The value
   *  is represented as a 32-bit Integer value, taking the form:
   *  	(MSB) TTTT TTTT RRRR RRRR GGGG GGGG BBBB BBBB (LSB)
   *  where MSB is the most significant bit, LSB the least significant bit, T/R/G/B 
   *  are 8-bit representations of the Transparency, Red, Green, and Blue colour values
   *  respectively.
   *  
   *  @param fileName The path where to save the image file
   *  @param image A 2-D array of pixel RGB values representing the image
   *  @returns True upon completion
   *  @throws IOException if the image cannot be found at the specified path
   *  @throws NullPointerException if the given filename is null
   *  
   *  @author Francesco Giovannini
   */
  def saveImage(fileName : String, image : Array[Array[Int]]) : Boolean = {
    // Get image width and height
    val height = image.length;
    var width = 0;
    if (height > 0) {
      width = image(0).length;
    }
    
    // Flatten the array
    var image1D = image.flatten;
       
    // Create temporary BufferedImage
    var buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    buffImg.setRGB(0, 0, width, height, image1D, 0, width);
    
    // Save image to file
    try {
      var outputFile = new File(fileName);
      ImageIO.write(buffImg, "jpg", outputFile);
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

  /** Efficiently convert a BufferedImage to a 2-D array of pixels.
   *  
   *  The complexity of this is still O(Width*Height), however the conversion from RGB
   *  value is more efficient, thus the code execution is faster than looping through
   *  each pixel and calling BufferedImage.getRGB() on them.
   *   
   *  This code was taken from: http://stackoverflow.com/a/9470843 and then adapted
   *  to the scala syntax.
   *
   *  @param image The BufferedImage object to be converted
   *  @returns A 2-D array of pixel RGB values representing the image
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
         for (pixel <- 0 to pixels.length - 1 by pixelLength) {
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
  
  /** Display the given image in a JFrame.
   *  
   *  @author Francesco Giovannini
   */
  def showImage(image : BufferedImage, title : String) : Boolean = {
    
    var frame = new JFrame() : JFrame;
    frame.setTitle(title);
    frame.getContentPane().setLayout(new FlowLayout());
    frame.getContentPane().add(new JLabel(new ImageIcon(image)));
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    return true
  }
}
