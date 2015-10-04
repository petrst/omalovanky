import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
/*
 * ImagePattern.java
 *
 * Created on May 7, 2007, 2:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author psturc
 */
public class ImagePattern implements Pattern {
	
	private BufferedImage image;
	
	/** Creates a new instance of ImagePattern */
	public ImagePattern(Image img) {
		int width = img.getWidth(null); 
		int height = img.getHeight(null); 
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = this.image.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
	}
	
	public int getRGB(int x, int y){
		  int width = image.getWidth();
		  int height = image.getHeight();
		  
		  return image.getRGB(x%width,y%height);
	}
	
	public Image getImage(){
		return image;
	}

}
