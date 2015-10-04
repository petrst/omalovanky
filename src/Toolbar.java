/*  $Id$
 *
 *  2004 (c) Copyrigth Petr Sturc  <psturc@axalto.com>
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Toolbar extends JPanel  {

    private static final Dimension buttonSize = new Dimension( 32, 32 );
    private static Toolbar _instance = null;

    
	private Toolbar(){
	}

    
    public static Toolbar getInstance(){
        if ( _instance == null ){
                _instance = new Toolbar();
                _instance.initUI();
        }
        return _instance;
    }
   
    
    
    private void initUI(){
        //setLayout( new GridLayout(0,2));
        //setMinimumSize( new Dimension( 96, 96 ) );
        //setMaximumSize( new Dimension( 96, Short.MAX_VALUE ) );
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS  ));
        //setBackground( Color.WHITE );
        //setBackground(new Color(0x7a, 0x8a, 0xb7));
        setBackground(new Color(0xbc, 0xc4, 0xdb));
        
        setBorder( BorderFactory.createEmptyBorder(5,5,5,5));
/*
        JButton exitButton = new EasyClickButton( Util.loadIcon("exit.png"));
        exitButton.setBackground( Color.WHITE );
        exitButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }});
        
        add(exitButton);
 */

        add(Box.createVerticalGlue());
try {
        BufferedReader in = new BufferedReader(new FileReader("resources/palette.rgb"));
        String line;
        while ( (line=in.readLine())!=null ){
            if ( line.startsWith("#") || line.length()==0 ) continue;
            int r = Integer.parseInt( line.substring(0,2), 16 );    
            int g = Integer.parseInt( line.substring(2,4), 16 );    
            int b = Integer.parseInt( line.substring(4,6), 16 );    
            add( new ColorButton( new Color( r, g, b ) ));
        }
	add( new ColorButton( new ImagePattern(new ImageIcon("chick.gif").getImage())));
	add( new ColorButton( new ImagePattern(new ImageIcon("chick1.gif").getImage())));
	add( new ColorButton( new ImagePattern(new ImageIcon("m45a.gif").getImage())));
        in.close();
} catch ( Throwable e ){
e.printStackTrace();
}

        //
        //add(Box.createHorizontalGlue());
        add(Box.createVerticalGlue());
     

        }
    

}

// vim:ts=4:sw=4

