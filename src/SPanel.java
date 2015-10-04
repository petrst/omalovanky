import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

class SPanel extends JPanel implements Runnable {
	
	private int vHeight = 0;
	private boolean visible = false;
		
	public SPanel(){	
		new Thread( this ).start();
	}
	
	
	public void setMVisible( boolean visible ){
		this.visible = visible ;
	}
	
	public boolean isMVisible(){
		return visible;
	}
	
	public void paint( Graphics g ){
		
		Rectangle r = getBounds();
		//getParent().repaint(r.x, r.y, r.width, r.height );
		g.setColor( Color.RED );
		g.fillRect( r.x, r.y, r.width, vHeight );
		
	}
	
	public void run(){
		
		while( true ){
			Rectangle r = getBounds();
			System.out.println("VIS: "+visible+" H: "+vHeight);
			if ( isMVisible() && vHeight < r.height ){
				vHeight+=7;
				repaint(r.x, r.y, r.width, r.height );
			} else if ( !isMVisible() && vHeight > 0 ){
				vHeight-=7;
				getParent().repaint( );
				repaint(r.x, r.y, r.width, r.height );
			}
			
			try {
				Thread.sleep( 50 );
			} catch ( InterruptedException e ){
			}
		}
		
	}
	
}