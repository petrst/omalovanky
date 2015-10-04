/*  $Id$
 *
 *  2004 (c) Copyrigth Petr Sturc  <psturc@axalto.com>
 *
 */

import java.applet.*;
import javax.swing.*;
import java.net.URL;

public class Util   {

    public static ImageIcon loadIcon( String name ){
        
            ImageIcon icon = null;
             URL iconURL = ClassLoader.getSystemResource("resources/" + name);
             if (iconURL != null) icon = new ImageIcon(iconURL);
             System.out.println("Loading "+name+"="+icon);
             return icon;
    }

    public static AudioClip loadClip( String name ){
        try {
             URL clipURL = ClassLoader.getSystemResource("resources/" + name);
             return Applet.newAudioClip( clipURL );
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }


}

// vim:ts=4:sw=4

