package com.tncy.top.image

import com.tncy.top.image;

import java.io.File
import javax.imageio.ImageIO


object Test extends App {
  
  override def main(args : Array[String]) {
    val imgpath : String = "/home/fgiovann/Documents/TelecomNancy/telecomnancy/top/RoadDetection/images/";
    var fileName : String = imgpath + "Tears_In_Rain_600px.png";
    //var imageLoader = AerialImage;
    
    // Load wrapped image
    var wrappedImage : ImageWrapper = new ImageWrapper(fileName);
    var image2D : Array[Array[Int]] = wrappedImage.getImage();
    for (row <- 0 to 40) {
      for (col <-0 to 80) {
        image2D(row)(col) = 0x000000FF;
      }
    }
    wrappedImage.saveImage(imgpath + "Tears_In_Rain_modded.png");
    println(wrappedImage);
    println(wrappedImage.height);
    println(wrappedImage.width);
    
    val sourceimage = new File(fileName) : File;
    var loadedImage = ImageIO.read(sourceimage);
    
    //imageLoader.showImage(loadedImage, "Loaded Image"); 
    
    //var image2D = imageLoader.loadImage(fileName);
    //imageLoader.saveImage("/home/fgiovann/Documents/TelecomNancy/telecomnancy/top/RoadDetection/images/anarchy_200px_rebuilt.jpg", image2D);
  }
}
