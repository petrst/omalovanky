import java.awt.Color;
/*
 * SolidPattern.java
 *
 * Created on May 7, 2007, 2:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author psturc
 */
public class SolidPattern implements Pattern {
	
	private int color;
	
	/** Creates a new instance of SolidPattern */
	public SolidPattern(Color color) {
		this.color = color.getRGB();
	}
	
	public int getRGB(int x, int y){
		return color;
	}
	
}
