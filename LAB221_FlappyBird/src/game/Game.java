package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JFrame;





/**
 *
 * @author Ninh
 */
public class Game implements MouseListener,KeyListener{
	private final int WIDTH = 600;
	private final int HEIGHT = 900;
	private final int SKY_ZONE = HEIGHT - HEIGHT/5;
	
	private int fallingVelocity;
	private GameRender playZone;
	private JFrame frame;
	private Rectangle frog;
	private Queue<Rectangle> obstacle;
	
	private boolean isOver = false;
	private boolean isStarted = false;
	private boolean isRelease = true; // block multi-jump in keyboard
	public Game(){
		initInterface();
		
		frame.addMouseListener(this);
		frame.addKeyListener(this);
		obstacle = new LinkedList<>();
		new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						gameRun();
						sleep(30);						
					} catch (Exception e) {
					}
				}
			}	
		}.start();
	}
	
	void initInterface(){
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
        frame.setTitle("Flappy Bird 2077");
        frame.setResizable(false);
		frame.setVisible(true);
		
		playZone = new GameRender(this);
		frame.add(playZone);
		playZone.setBackground(new Color(151, 247, 252));
		// Initialize the frog object(a rectangle)
		frog = new Rectangle(WIDTH/2 - 10, HEIGHT*2/5 - 10, 20, 20);
		
	}
	
	void jump(){
		if(!isOver){
			
			// If game not started yet, init the falling from 10
			if (!isStarted) {
				fallingVelocity = 10;
				isStarted = true;
			}
			// velocity is negative -> the frog go up
			fallingVelocity = -10;
		}
	}
	
	void gameRun() {
		if (isStarted) {
			// Block the falling velocity(max 10)
			if (fallingVelocity < 11)
				fallingVelocity += 1;
		}
		// Update the location the frog after every Thread loop (apply motion of the frog)
		frog.y += fallingVelocity;
	
		// Block top
		if (frog.y <= 0) {
			if (fallingVelocity < 0)
				fallingVelocity = 0;
		}
		// Block land
		if (frog.y + 20 > SKY_ZONE) {
			frog.y = SKY_ZONE - 20;
			fallingVelocity = 0;
			isStarted = false;
		}
		playZone.repaint();
	}
	
	
	
	/**
	 * Extend code in paintComponent in GamePanel
	 * @param g 
	 */
	void repaint(Graphics g) {
				
		// Create grass
		g.setColor(Color.green);
		g.fillRect(0, SKY_ZONE, WIDTH, HEIGHT/5);
		// Create land
		g.setColor(new Color(252, 244, 151));
		g.fillRect(0, HEIGHT - HEIGHT/6, WIDTH, HEIGHT/6);
		//create frog
		g.setColor(Color.PINK);
		g.fillRect(frog.x, frog.y, frog.width, frog.height);
	}
	
	public static void main(String[] args) {
		Game g = new Game();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == ' ' && isRelease){
			jump();
			isRelease = false;
		}
		if(e.getKeyChar() == 'r' && isRelease){
			System.out.println("a");
			isOver = false;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		isRelease = true;
	}

}
