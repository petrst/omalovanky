/* $Id$
 *
 *  2004 (c) Copyrigth Petr Sturc  <psturc@axalto.com>
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;


public class EasyClickButton extends JButton implements MouseListener {

    
    private static final Dimension buttonSize = new Dimension(32, 32);
    private static final Border normalBorder = BorderFactory.createEtchedBorder(
            Color.WHITE, Color.GRAY);
    private static final Border focusedBorder = BorderFactory.createEtchedBorder(
            Color.RED, Color.YELLOW);

   
    public EasyClickButton( ImageIcon icon ) {
        super( icon );
        setBorder(normalBorder);
        setMinimumSize(buttonSize);
        setPreferredSize(buttonSize);
        setMaximumSize(buttonSize);
        addMouseListener(this);    
    }

   
    public void mousePressed(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {
    }
        
    public void mouseEntered(MouseEvent e) {
        setBorder(focusedBorder);
    }

    public void mouseExited(MouseEvent e) {
        setBorder(normalBorder);
    }
            
    public void mouseReleased(MouseEvent e) {
	if ( e.getButton()!=MouseEvent.BUTTON1 ){
		doClick();   
	}
    }
}

// vim:ts=4:sw=4

