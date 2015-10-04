/*  $Id$
 *
 *  2004 (c) Copyrigth Petr Sturc  <psturc@axalto.com>
 *
 */

import java.io.*;

public class ImageList   {


    private static ImageList _instance = null;
    private FilenameFilter filter = null;   
    private File imgDir = null; 
    private File[] images;
    private int current = 0;


    public static ImageList getInstance(){
        if ( _instance == null ){
            _instance = new ImageList();
        }
        return _instance;
    }
     
	private ImageList(){
		filter = new ExtFilter(".gif");
		imgDir = new File("./images/");
		init();
	}

    
    public void init() {
        images = imgDir.listFiles( filter );
	System.out.println("IMG LEN: " + images.length);
    }
    
    public int size(){
        return images.length;
    }    
   
   
    public int getIndex(){
        return current;
    }
 
  public String prev(){
       String f=null;
       try { 
        if ( images != null ){
           current = ( current - 1 ) % images.length;
           f = images[current].getCanonicalPath();
	   System.out.println("("+current+") "+f);

        } 
       } catch ( IOException e ) {
         f=null; 
       }
       return f;
    }
    
   public String next(){
       String f=null;
       try { 
        if ( images != null ){
           current = ( current + 1 ) % images.length;
           f = images[current].getCanonicalPath();
	   System.out.println("("+current+") "+f);
        } 
       } catch ( IOException e ) {
         f=null; 
       }
       return f;
    }

       static class ExtFilter implements FilenameFilter {
        
        private String ext;
        
        public ExtFilter( String extension ){
            this.ext = extension.toLowerCase();
        }
        
        public boolean accept( File dir, String name ){
            int i = name.toLowerCase().indexOf(ext);
            return ( i == name.length()-ext.length() );
        }
    }

}

// vim:ts=4:sw=4
