package controller;

import entity.*;
import game.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Ninh
 */
public class GameController implements MouseListener{
	private Frog frog;										
	private LinkedList<Pipe> pipes;
	private GameMemory memory;
	
	private Game game;
//	private JLabel frog;
	private JPanel playZone;
	private JLabel labelPoint;

	private int fallingVelocity = 10;

	private boolean isRunning;
	private boolean hasOver;
	private boolean hasStart;
	private int point;

	public boolean hasOver() {
		return hasOver;
	}

	public void setOver(boolean hasOver) {
		this.hasOver = hasOver;
	}

	public boolean hasStart() {
		return hasStart;
	}

	public void setStart(boolean hasStart) {
		this.hasStart = hasStart;
	}

	public boolean hasRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public GameController() {
	}

	public GameController(Game game) {
		this.game = game;
		pipes = new LinkedList<>();
		point = 0;
		initInteface();
		playZone.addMouseListener(this);
		// Frog's Thread 
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1);
						if(hasStart)
							frogMotion();
						Thread.sleep(29);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
		// Game's Thread
		new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(1);
						if(isRunning){
							pipesMotion();
						}
						Thread.sleep(9);
					} catch (Exception e) {
					}
				}
			}
			
		}.start();
	}
	private void initInteface(){
		playZone = game.getPlayZone();
		labelPoint = game.getLabelPoint();
		labelPoint.setText("Points: 0");
		frog = new Frog(playZone);
		initPipes();
	}
	private void initPipes(){
		for (int i = 0; i < 5; i++) {
			addPipe();
		}
	}

	private void addPipe() {
		Pipe pipe = new Pipe(playZone, pipes);
		pipes.add(pipe);
	}


	private void pipesMotion(){
		for(Pipe pipe : pipes){
			pipe.move(2);
			int location = pipe.getTop().getX();
			if (frog.hasIntersect(pipe)) {
				isRunning = false;
				hasOver = true;
				return;
			}
 			if(location <= playZone.getWidth()/3 && !pipe.hasCount()){
				labelPoint.setText("Points: " + ++point);
				pipe.setCount(true);
			}
			if(location + pipe.WIDTH < 0){
				pipes.removeFirst();
				addPipe();
			}
		}
		
	}
	public void jump() {
		if(!hasStart){
			hasStart = true;
		}
		if (!hasOver) {
			isRunning = true;
			fallingVelocity = -10;
		}
	}
	private void frogMotion() {
		if (fallingVelocity < 12) {
			fallingVelocity += 1;
		}

		frog.fall(fallingVelocity);
		
		// Block top
		if(frog.getY() <= 0){
			fallingVelocity = 0;
			frog.setLocation(frog.getX(),0);
		}
		// Block land
		if (frog.getY() >= playZone.getHeight()-frog.getHeight()) {
			fallingVelocity = 0;
			frog.setLocation(frog.getX(), playZone.getHeight()-frog.getHeight());
			isRunning = false;
			hasStart = false;
			gameOver();
		}
	}
	private void gameOver(){
		hasOver = true;
		JOptionPane.showMessageDialog(playZone, "Score: " + point, "GAMEOVER", JOptionPane.PLAIN_MESSAGE);
		newGame();
	}
	private void newGame(){
		hasOver = false;
		playZone.removeAll();
		point = 0;
		pipes.clear();
		initInteface();
		playZone.updateUI();
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
}
