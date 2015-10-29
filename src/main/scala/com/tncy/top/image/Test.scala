package com.tncy.top.image

import com.tncy.top.image;

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.FlowLayout; 


object Test extends App {
  val imgpath : String = "/home/fgiovann/Documents/TelecomNancy/telecomnancy/top/RoadDetection/images/";
  var fileName : String = imgpath + "Tears_In_Rain_600px.png";
  //var imageLoader = AerialImage;
  
  // Load wrapped image
  var wrappedImage : ImageWrapper = new ImageWrapper(fileName);
  var image2D : Array[Array[Int]] = wrappedImage.getImage();
  //println(wrappedImage);
  println("The image height is: " + wrappedImage.height + " px.");
  println("The image width is: " + wrappedImage.width + " px.");
  
  // Show loaded image
  val sourceimage = new File(fileName) : File;
  var loadedImage = ImageIO.read(sourceimage); 
  showImage(loadedImage, "Loaded Image"); 
  
  // Modify image
  for (row <- 0 to 40) {
    for (col <-0 to 80) {
      image2D(row)(col) = 0x000000FF;
    }
  }

  // Save image
  val outPath = imgpath + "Tears_In_Rain_modded.png";
  wrappedImage.saveImage(outPath);
  
  // Show saved image
  val outimage = new File(outPath) : File;
  var savedImage = ImageIO.read(outimage); 
  showImage(savedImage, "Saved Image"); 

  /** Display the given image in a JFrame.
   *  
   *  @author Francesco Giovannini
   */
  private def showImage(image : BufferedImage, title : String) : Boolean = {
    
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
