package game;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Ninh
 */
public class GameRender extends JPanel{

	private Game happyFrog;

	public GameRender(Game happyFrog) {
		this.happyFrog = happyFrog;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		happyFrog.repaint(g);
	}

}
