/*  $Id$
 *
 *  2004 (c) Copyrigth Petr Sturc  <psturc@axalto.com>
 *
 */


import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Crayon   {

    protected Image tImg;
    protected BufferedImage img;

    public Crayon(String filePath){
        loadImage( filePath );	
     }

    private void loadImage( String fileName ){
        
        // ImageIcon loads image synchronously. In fact
        // it uses MediaTracker too.
        tImg = Util.loadIcon(fileName).getImage();
        //tImg = new ImageIcon(fileName).getImage();
        restoreOriginal();
    }
    

    private void restoreOriginal(){
         
        int iw = tImg.getWidth(null);
        int ih = tImg.getHeight(null);

        img = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.drawImage(tImg, 0, 0, null);
        g.dispose();
    }

    private boolean isGray( int c ){
        int r = ( c & 0x00ff0000  ) >> 16;
        int g = ( c & 0x0000ff00  ) >> 8;
        int b = ( c & 0x000000ff  );

        if ( r==g && g==b ) return true;

        return false;
    }
       
    public void setColor( Color c ){
        
        restoreOriginal();
         
        int iw = img.getWidth();
        int ih = img.getHeight();
         
        for ( int x=0; x<iw; x++ ){
            for ( int y=0; y<ih; y++ ){
                int pix = img.getRGB(x,y);
                
                if ( isGray(pix) ){       

                    // get hue of the new color
                    Color pixC = new Color(pix);
                    float[] newHsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);                    
                    float[] pixHsb = Color.RGBtoHSB(pixC.getRed(), pixC.getGreen(), pixC.getBlue(), null);

                    int newC = Color.HSBtoRGB( newHsb[0], newHsb[1], pixHsb[2] );
                    
                    // preserve alpha in shadows
                    newC = (newC & 0x00ffffff) +  ( pix & 0xff000000 );
                    img.setRGB( x,y, newC );

                    //System.out.println("["+pixHsb[0]+"x"+pixHsb[1]+"x"+pixHsb[2]+"]");
                    //System.out.println("{"+newHsb[0]+"x"+newHsb[1]+"x"+newHsb[2]+"}");
                }
            }
        }
    }
    
    public void draw( Graphics2D g, int x, int y ){
        g.drawImage( img, x, y, null);
    }
    
}

// vim:ts=4:sw=4

