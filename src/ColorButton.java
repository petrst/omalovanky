/* $Id$
 *
 *  2004 (c) Petr Sturc  <psturc@axalto.com>
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.applet.*;
import java.io.*;


public class ColorButton extends JButton implements MouseListener {

    private static ColorButton selected;
    
    private static AudioClip clip; 
    private static final Dimension buttonSize = new Dimension(32, 32);
    private static final Border normalBorder = BorderFactory.createEtchedBorder(
            Color.WHITE, Color.GRAY);
    private static final Border focusedBorder = BorderFactory.createEtchedBorder(
            Color.RED, Color.YELLOW);

    static {
        clip = Util.loadClip("clip3.wav");

    }

    private Pattern pattern;
    
    public ColorButton(Color color) {
        setBackground(color);
        setBorder(normalBorder);
        setMinimumSize(buttonSize);
        setPreferredSize(buttonSize);
        setMaximumSize(buttonSize);
        addMouseListener(this);    
	this.pattern = new SolidPattern( color );
    }

    public ColorButton(ImagePattern pattern) {
	    this( Color.WHITE );
	    this.pattern = pattern;
	    setIcon( new ImageIcon(pattern.getImage()));
    }
    
    public boolean isSelected() {
        return (selected == this);
    }
    
    public void selectThis() {
        if (selected != null) {
            selected.setBorder(normalBorder);
        }
        selected = this;
        selected.setBorder(focusedBorder);
    }

    public void mousePressed(MouseEvent e) {
        if (clip != null) {
            clip.play();
        }
        selectThis();
        doClick(250);
        //ImagePanel.getInstance().setFillColor(getBackground());
	//if ( pattern!=null)
	ImagePanel.getInstance().setFillPattern(pattern);
    }

    public void mouseClicked(MouseEvent e) {}
        
    public void mouseEntered(MouseEvent e) {
        setBorder(focusedBorder);
    }

    public void mouseExited(MouseEvent e) {
        if (!isSelected()) {
            setBorder(normalBorder);
        }
    }
            
    public void mouseReleased(MouseEvent e) {
    }
}

// vim:ts=4:sw=4

