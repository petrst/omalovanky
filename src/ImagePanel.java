/* $Id$
 *
 *  2004 (c) Copyrigth Petr Sturc  <psturc@axalto.com>
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import java.io.*;
import java.awt.print.*;


public class ImagePanel extends JPanel implements MouseMotionListener, MouseListener, Printable {

    private static ImagePanel _instance = null;
    private static BufferedImage image = null;
    private static MediaTracker tracker;
    private boolean loading = false;
    private boolean showCursor = false;
    private JButton exitButton, prevButton, nextButton, printButton;
    private JPanel sPanel;

    private Image cursor;
    private Crayon crayon;
    
    private Color fillColor = Color.white;
    
    private Pattern fillPattern;
    
    private static final Dimension buttonSize = new Dimension( 32, 32 );
    private int wx1, wx2, wy1, wy2;

    private Stack stack;

    private int x, y;
    private int dx, dy, w, h;
    
    private FloodFill fill;
    
    private boolean clip;
    private int minX, minY, maxX, maxY;
    
    public ImagePanel() {
        tracker = new MediaTracker(this);
        // addMouseMotionListener(this);
        setBorder(new EtchedBorder());
        addMouseListener(this);
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent evt) {
                x = evt.getX();
                y = evt.getY();
//		minX = Math.min( minX, x);
//		maxX = Math.max( maxX, x+220);
//		minY = Math.min( minY, y-63);
//		maxY = Math.max( maxY, y-63+141);
//		clip = true;
                repaint();
            }
        });
        
    }
    
    public static ImagePanel getInstance() {
        if (_instance == null) {
            _instance = new ImagePanel();
            _instance.initUI();
        }
        return _instance;
    }
    
    public void setFillColor(Color c) {
        fillColor = c;
        if ( crayon!=null) crayon.setColor(c);
    }
 
       public void loadImage(String imagePath) {
        
        loading = true;
        
        // ImageIcon loads image synchronously. In fact
        // it uses MediaTracker too.
        Image img = new ImageIcon(imagePath).getImage();
	Image pattern = new ImageIcon("resources/chick.gif").getImage();
        img = adjust(img);

        // Scaling operation is lazy and actualy it is
        // done only when needed. Let's force it using
        // MediaTracker
        tracker.addImage(img, 0);
	tracker.addImage(pattern, 0);
        try {
            tracker.waitForAll();
        } catch (Exception e) {}

        image = new BufferedImage(wx2 - wx1 + 1, wy2 - wy1 + 1,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.drawImage(img, 0, 0, this);
        g.dispose();

        loading = false; 
	fill = new FloodFill( image );
	fill.setAntialiased(true);
	fill.setPatternImage(pattern);
        repaint();
    }
 
    // Scales the image so it fits the canvas.
    // The picture will be palced in the center.
    private Image adjust(Image orig) {
        
        int iw = orig.getWidth(this);
        int ih = orig.getHeight(this);
        
        Insets i = getInsets();
        int cw = getSize().width-i.left-i.right; // Canvas width
        int ch = getSize().height-i.top-i.bottom; // Canvas height

        if (iw > cw || ih > ch) {
            // scale
            
            double scaleW = 1.0 * cw / iw;
            double scaleH = 1.0 * ch / ih;
           
            if (scaleW < scaleH) {
                // Fit width
                wx1 = i.left;
                wx2 = cw - 1;
            
                int nH = (int) (ih * scaleW);

                wy1 = (ch - nH) / 2;
                wy2 = wy1 + nH - 1;
            } else {
                // Fit height
                
                int nW = (int) (iw * scaleH);

                wx1 = (cw - nW) / 2;
                wx2 = wx1 + nW - 1;
            
                wy1 = i.top;
                wy2 = ch - 1;
            }
            /*
            System.out.println(
                    "ADJUST sCALE: " + wx1 + "@" + wy1 + " => " + wx2 + "@"
                    + wy2);
            */        
            return orig.getScaledInstance(wx2 - wx1, wy2 - wy1,
                    Image.SCALE_DEFAULT);
        } else {
            // center
            wx1 = (cw - iw) / 2;
            wy1 = (ch - ih) / 2;
            wx2 = wx1 + iw - 1;
            wy2 = wy1 + ih - 1;

            /*
            System.out.println(
                    "ADJUST cENTER: " + wx1 + "@" + wy1 + " => " + wx2 + "@"
                    + wy2);
             */
            return orig;
        }

    }
    
    private void initUI() {
        setBackground(Color.WHITE);
        setFillColor(Color.red);
        setBorder( BorderFactory.createEmptyBorder(5,5,5,5));
        setLayout( null );
        
        int[] pixels = new int[16 * 16];
        Image image = Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(16, 16, pixels, 0, 16));
        Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor
                (
                image, new Point(0, 0), "invisiblecursor");

        setCursor(transparentCursor);

        cursor = Util.loadIcon("paintbrush.png").getImage();
        crayon = new Crayon("crayon.png");
        
      
        exitButton = new EasyClickButton( Util.loadIcon("exit.png"));
        exitButton.setBackground( Color.WHITE );
        exitButton.setCursor( Cursor.getDefaultCursor()); 
        exitButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }});
        
        add(exitButton);
 
	prevButton = new EasyClickButton( Util.loadIcon("prev.png"));
	prevButton.setBackground( Color.WHITE );
        prevButton.setCursor( Cursor.getDefaultCursor()); 
        prevButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ImagePanel.getInstance().loadImage( ImageList.getInstance().prev() );
            }});
        
        add(prevButton);
	    
        nextButton = new EasyClickButton( Util.loadIcon("array.png"));
        nextButton.setBackground( Color.WHITE );
        nextButton.setCursor( Cursor.getDefaultCursor()); 
        nextButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ImagePanel.getInstance().loadImage( ImageList.getInstance().next() );
            }});
        
        add(nextButton);

    printButton = new EasyClickButton( Util.loadIcon("printer.png"));
    printButton.setBackground( Color.WHITE );
    printButton.setCursor( Cursor.getDefaultCursor()); 
    printButton.addActionListener( new ActionListener(){
        public void actionPerformed(ActionEvent e){
            //ImagePanel.getInstance().loadImage( ImageList.getInstance().next() );
            // DO PRINT
            if (e.getSource() instanceof JButton) {
                PrinterJob printJob = PrinterJob.getPrinterJob();
                PageFormat pf = new PageFormat();
                pf.setOrientation(PageFormat.LANDSCAPE);
                printJob.setPrintable(_instance, pf);
                //if (printJob.printDialog()) {
                    try {
                        printJob.print();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                //}
            }
        }});
        
    add(printButton);
    

/*	    
	sPanel = new SPanel();
	sPanel.setBounds(0,0,100,100);
	sPanel.addMouseListener( new MouseAdapter(){
			public void mouseEntered( MouseEvent e){
				((SPanel)e.getSource()).setMVisible(true);
			}
			
			public void mouseExited( MouseEvent e){
				((SPanel)e.getSource()).setMVisible(false);
			}

	});
	add( sPanel );
*/

	
    }

    public void paintComponent(Graphics g) {
	
	if ( clip ){
		clip = false;
		//g.setClip(x, y-63, 220, 141);
		g.setClip(minX, minY, maxX-minX, maxY-minY);
	}
	    
        super.paintComponent(g);
        Insets i = getInsets();

        int cw = getSize().width-i.left-i.right; // Canvas width
        int ch = getSize().height-i.top-i.bottom; // Canvas height


        exitButton.setBounds( cw - 37, 5, 42, 42 );
        printButton.setBounds( cw - 80, 5, 42, 42 );
        
        prevButton.setBounds( cw - 80, ch - 37, 42, 42 );
        nextButton.setBounds( cw - 37, ch - 37, 42, 42 );
            
        
        if (loading) {
            // g.clearRect(0, 0, d.width, d.height);
            g.drawString("Please wait...", 0, 10);
        } else {
                //g.setColor(new Color(128,156,201));
                g.setColor(new Color(0xbc, 0xc4, 0xdb));
                for ( int by = 0; by < ch+i.top+i.bottom; by+=3 ){
                   g.drawLine( 0, by, cw+i.left+i.right, by );
                }

                g.setColor(new Color(0x7a, 0x8a, 0xb7));
                g.drawLine( cw+i.left+i.right-1, 0,  cw+i.left+i.right-1, ch+i.top+i.bottom );
                g.drawLine( 0, 0,  0, ch+i.top+i.bottom );
    
            if ( image==null){ loadImage( "images/omalovanky.png" ); }
                
            if (image != null) {
                        
                //g.drawImage(image, wx1, wy1, this);
                g.setColor( Color.WHITE);
                g.fillRect(wx1-2, wy1-2, wx2 - wx1+6, wy2 - wy1+6);
                
                g.setColor(new Color(0x7a, 0x8a, 0xb7));
                g.drawRect(wx1, wy1, wx2 - wx1+2, wy2 - wy1+2);
                g.setColor(new Color(0xbc, 0xc4, 0xdb));
                g.drawRect(wx1-2, wy1-2, wx2 - wx1+6, wy2 - wy1+6);

                g.drawImage( image, wx1+1, wy1+1, this );
                   
                
             //   g.setColor(new Color(0x7a, 0x8a, 0xb7));
             //   g.drawLine( cw+i.left+i.right-1, 0,  cw+i.left+i.right-1, ch+i.top+i.bottom );
            }

            if (showCursor) {
                g.setColor(fillColor);
                //g.fillOval(x - 3, y - 3, 6, 6);
                // g.setClip(clip);
                //g.drawImage(cursor, x, y - 20, null);
                crayon.draw((Graphics2D)g, x, y-63);
            }
        }
    }


    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        if (pi >= 1) {
            return Printable.NO_SUCH_PAGE;
        }
        
        //Graphics g2 = button.getGraphics();
        //button.printAll(g2);
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        
        int w = (int) pf.getImageableWidth(); // pf is a PageFormat object.
        int cw = image.getWidth();        
        double factorW = (double) w/((double) cw);
        
        int h = (int) pf.getImageableHeight(); // pf is a PageFormat object.
        int ch = image.getHeight();        
        double factorH = (double) h/((double) ch);
        
        double factor = Math.min( factorW, factorH );
        
        g2d.scale(factor, factor);
        g2d.drawImage(image, 0,0, null);
        
        return Printable.PAGE_EXISTS;
    }
  
  
    
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int rgb = image.getRGB(x, y);

        // image.setRGB(x,y,-1816940);
        // repaint();
        //System.out.println(x + "/" + y + ": " + rgb);
    }

    public void mouseEntered(MouseEvent e) {
        showCursor = true;
    }

    public void mouseExited(MouseEvent e) {
        showCursor = false;
        repaint();
    }

    public void mousePressed(MouseEvent e) {
            int x = e.getX();
        int y = e.getY();

        //fill(x, y, fillColor.getRGB());
	//fill.fill(x-wx1,y-wy1, fillColor);
	System.out.println(fillPattern);
	fill.fill(x-wx1,y-wy1, fillPattern);
	image = fill.getImage();
        repaint();

    }

    public void mouseReleased(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {
    }
   
    final private int pixelread(int x, int y) {
        return image.getRGB(x - wx1, y - wy1);
    }

    final private void pixelwrite(int x, int y, int p) {
//	
//	Color oc = new Color(image.getRGB(x-wx1,y-wy1));    
//	System.out.println("oc="+oc);
//	Color nc = new Color(p);
//	Color rc = new Color( oc.getRed()*nc.getRed()/255, oc.getGreen()*nc.getGreen()/255, oc.getBlue()*nc.getBlue()/255);
//	if ( p!=rc.getRGB()){
//		System.out.println(p+"->"+rc.getRGB());
//	}
        image.setRGB(x - wx1, y - wy1, p);
    }

    final private void push(int y, int xl, int xr, int dy) {
        if (y + dy >= wy1 && y + dy <= wy2) {
            stack.push(new int[] { y, xl, xr, dy});
        }
    }
   
    /* The following is a rewrite of the seed fill algorithm by Paul Heckbert
     * The original was published in "Graphics Gems", Academic Press, 1990.
     * 
     * Adapted for Java by  Markku Savela <msa@anise.tte.vtt.fi>
     *  
     */    
    private void fill(int x, int y, int nv) {
        int l, x1, x2, dy;
        int ov;
  
        stack = new Stack();
        ov = pixelread(x, y); /* read pv at seed point */
        //System.out.println("Calling Fill: " + x + "@" + y + " " + ov + "/" + nv);
        if (ov == Color.black.getRGB() || ov == nv || x < wx1 || x > wx2
                || y < wy1 || y > wy2) {
            return;
        }

        push(y, x, x, 1);
        push(y + 1, x, x, -1); /* seed segment (popped 1st) */

        while (!stack.empty()) {

            /* pop segment off stack and fill a neighboring scan line */
            int[] top = (int[]) stack.pop();

            dy = top[3];
            y = top[0] + dy;
            x1 = top[1];
            x2 = top[2];
 
            /*
             * segment of scan line y-dy for x1<=x<=x2 was
             * previously filled, now explore adjacent pixels
             * in scan line y
             */
            for (x = x1; x >= wx1 && pixelread(x, y) == ov; x--) {
                pixelwrite(x, y, nv);
            }
            if (x >= x1) {
                for (;;) {
		    for (x++; x <= x2 && pixelread(x, y) == Color.BLACK.getRGB(); x++) {			
                    //for (x++; x <= x2 && pixelread(x, y) != ov; x++) {
                        ;
                    }
                    l = x;
                    if (x > x2) {
                        break;
                    }
		    for (; x <= wx2 && pixelread(x, y) != Color.BLACK.getRGB(); x++) {
                    //for (; x <= wx2 && pixelread(x, y) == ov; x++) {
                        pixelwrite(x, y, nv);
                    }
                    push(y, l, x - 1, dy);
                    if (x > x2 + 1) {
                        push(y, x2 + 1, x - 1, -dy);
                    }
                }
            } else {
                l = x + 1;
                if (l < x1) {
                    push(y, l, x1 - 1, -dy);
                }
                x = x1 + 1;
                do {
                    for (; x <= wx2 && pixelread(x, y) != Color.BLACK.getRGB(); x++) {			
                    //for (; x <= wx2 && pixelread(x, y) == ov; x++) {
                        pixelwrite(x, y, nv);
                    }
                    push(y, l, x - 1, dy);
                    if (x > x2 + 1) {
                        push(y, x2 + 1, x - 1, -dy);
                    }
		    for (x++; x <= x2 && pixelread(x, y) == Color.BLACK.getRGB(); x++) {
                    //for (x++; x <= x2 && pixelread(x, y) != ov; x++) {
                        ;
                    }
                    l = x;
                } while (x <= x2);
            }
        }
    }

	public void setFillPattern(Pattern fillPattern) {
		this.fillPattern = fillPattern;
		setFillColor( new Color(fillPattern.getRGB(0,0)) );
	}

}

// vim:ts=4:sw=4

