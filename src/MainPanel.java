/*  $Id$
 *
 *  2004 (c) Copyrigth Petr Sturc  <psturc@axalto.com>
 *
 */


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class MainPanel extends JPanel  {

	public MainPanel(){
	
	}

    public void initUI(){
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS  ));
        add( Toolbar.getInstance() );
        add( ImagePanel.getInstance() );
    }
   

	public static void main(String[] args){
        boolean fullscreen = true;
        JFrame mainWin = new JFrame("Omalovanky");
        mainWin.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice(); 
        
        if ( fullscreen && gd != null  ){
            mainWin.setUndecorated(true);
            mainWin.setResizable(false);
            
            // Make fullscreen
            gd.setFullScreenWindow(mainWin);
            mainWin.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            mainWin.setSize(300,300);
        }

        
        MainPanel p    = new MainPanel();
        p.initUI();
        mainWin.getContentPane().add( p );
        mainWin.setVisible( true );
	}

    
}

// vim:ts=4:sw=4

