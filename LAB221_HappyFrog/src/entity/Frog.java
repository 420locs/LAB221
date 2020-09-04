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

	public Frog() {
	}

	public Frog(JPanel playZone) {
		frog = new JLabel();
		ImageIcon object = new ImageIcon("./rsz_1dz.jpg");
		frog.setBounds(playZone.getWidth()/3, playZone.getHeight()/2, object.getIconWidth(), object.getIconHeight());
		frog.setIcon(object);
		playZone.add(frog);
	}
	
	public void fall(int velocity){
		frog.setLocation(frog.getX(), frog.getY() + velocity);
	}
	
	public void setLocation(int x, int y){
		frog.setLocation(x, y);
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
	
}
