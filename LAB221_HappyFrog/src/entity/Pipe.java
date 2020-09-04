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
	private boolean hasCount;

	public Pipe() {
	}

	public Pipe(JPanel playZone, LinkedList<Pipe> pipes) {
		hasCount = false;
		Random rand = new Random();
		int topHeight = 50 + rand.nextInt(200);
		int bottomHeight = playZone.getHeight()-SPACE-topHeight;
		
		top = new JButton();
		bottom = new JButton();
		if (pipes.isEmpty()) {
			top.setBounds(playZone.getWidth() + GAP, 0, WIDTH, topHeight);
			bottom.setBounds(playZone.getWidth() + GAP, topHeight + SPACE, WIDTH, bottomHeight);
		} else {
			top.setBounds(pipes.getLast().getTop().getX()+ GAP, 0, WIDTH, topHeight);
			bottom.setBounds(pipes.getLast().getBottom().getX() + GAP, topHeight + SPACE, WIDTH, bottomHeight);
		}
		
		playZone.add(top);
		playZone.add(bottom);
	}
	
	public void move(int velocity){	
		top.setLocation(top.getX() - velocity , top.getY());
		bottom.setLocation(bottom.getX() - velocity, bottom.getY());
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
	
	
	
	
}
