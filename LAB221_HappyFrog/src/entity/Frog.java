package entity;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Ninh
 */
public class Frog {
	private JLabel frog;
	private int fallingVelocity;
	public Frog() {
	}

	public Frog(ImageIcon icon, JPanel playZone) {
		fallingVelocity = 10;
		frog = new JLabel();
		frog.setBounds(playZone.getWidth()/3, playZone.getHeight()/2, icon.getIconWidth(), icon.getIconHeight());
		frog.setIcon(icon);
		playZone.add(frog);
	}
	
	public void fall(){
		frog.setLocation(frog.getX(), frog.getY() + fallingVelocity);
	}
	
	public void setLocation(int x, int y){
		frog.setLocation(x, y);
	}

	public int getFallingVelocity() {
		return fallingVelocity;
	}

	public void setFallingVelocity(int fallingVelocity) {
		this.fallingVelocity = fallingVelocity;
	}
	
	
	public int getX(){
		return frog.getX();
	}
	
	public int getY(){
		return frog.getY();
	}
	
	public int getWidth(){
		return frog.getWidth();
	}
	
	public int getHeight(){
		return frog.getHeight();
	}
	
	public boolean hasIntersect(Pipe pipe){
	if (frog.getBounds().intersects(pipe.getTop().getBounds()) || frog.getBounds().intersects(pipe.getBottom().getBounds())) 
		return true;
	return false;
	}

	public JLabel getFrog(){
		return frog;
	}
}
