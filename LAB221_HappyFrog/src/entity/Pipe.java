package entity;

import java.util.LinkedList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Ninh
 */
public class Pipe {

	public final int WIDTH = 40;
	public final int SPACE = 170;
	public final int GAP = 200;

	private JButton top;
	private JButton bottom;
	private int topHeight;
	private int bottomHeight;
	private int locationX;
	private boolean hasCount;

	public Pipe() {
	}
	
	public Pipe(int x, int topHeight, int bottomHeight){
		this.locationX = x;
		this.topHeight = topHeight;
		this.bottomHeight = bottomHeight;
		top = new JButton();
		bottom = new JButton();
		
		top.setBounds(locationX, 0, WIDTH, topHeight);
		bottom.setBounds(locationX, topHeight + SPACE, WIDTH, bottomHeight);
	}

	/**
	 * Generate a random Pipe.
	 * random a pipe have top = 50 + [0-200], bottom = panelHeight - SPACE(170) - top.
	 * @param playZone The panel that will add this Pipe
	 * @param pipes List of pipe will add this Pipe
	 * @return Random Pipe
	 */
	public Pipe generatePipe(JPanel playZone, LinkedList<Pipe> pipes){
		top = new JButton();
		bottom = new JButton();
		
		hasCount = false;
		Random rand = new Random();
		topHeight = 50 + rand.nextInt(200);
		bottomHeight = playZone.getHeight() - (topHeight + SPACE);
		
		if (pipes.isEmpty()) {
			locationX = playZone.getWidth() + GAP;
		} else {
			locationX = pipes.getLast().getTop().getX()+ GAP;
		}
		
		top.setBounds(locationX, 0, WIDTH, topHeight);
		bottom.setBounds(locationX, topHeight + SPACE, WIDTH, bottomHeight);
		playZone.add(top);
		playZone.add(bottom);
		return this;
	}
		
	
	public void move(int velocity){	
		locationX -= velocity;
		top.setLocation(locationX, top.getY());
		bottom.setLocation(locationX, bottom.getY());
	}

	public JButton getTop() {
		return top;
	}

	public void setTop(JButton top) {
		this.top = top;
	}

	public JButton getBottom() {
		return bottom;
	}

	public void setBottom(JButton bottom) {
		this.bottom = bottom;
	}
	
	public boolean hasCount() {
		return hasCount;
	}

	public void setCount(boolean hasCount) {
		this.hasCount = hasCount;
	}

	public void setTopHeight(int topHeight) {
		this.topHeight = topHeight;
	}

	public void setBottomHeight(int bottomHeight) {
		this.bottomHeight = bottomHeight;
	}

	public int getTopHeight() {
		return topHeight;
	}

	public int getBottomHeight() {
		return bottomHeight;
	}

	public int getLocationX() {
		return locationX;
	}
	
	
	
	
}
