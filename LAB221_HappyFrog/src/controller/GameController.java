package controller;

import game.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Ninh
 */
public class GameController implements MouseListener{

	private Game game;
	private JLabel frog;
	private JPanel playZone;

	private int fallingVelocity = 10;

	private boolean isRunning = true;					//True default for testing, change it after finished the test
	LinkedList<JButton> pipes;
	public GameController() {
	}

	public GameController(Game game) {
		this.game = game;
		pipes = new LinkedList<>();
		initInteface();
		playZone.addMouseListener(this);
		initPipes();
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						frogMotion();
						Thread.sleep(30);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
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
		
		frog = new JLabel();
		ImageIcon object = new ImageIcon("./rsz_1dz.jpg");
		frog.setBounds(playZone.getWidth()/2-frog.getWidth(), playZone.getHeight()/2-frog.getHeight(), object.getIconWidth(), object.getIconHeight());
		frog.setIcon(object);
		playZone.add(frog);
	}
	private void initPipes(){
		for (int i = 0; i < 5; i++) {
			addPipe();
		}
	}
	private void addPipe(){
		Random rand = new Random();
		int pipeWidth = 40;
		int space = 170;
		int gap = 200;
		int topHeight = 50 + rand.nextInt(200);
		int bottomHeight = playZone.getHeight()-space-topHeight;
		
		JButton top = new JButton();
		JButton bottom = new JButton();
		if (pipes.isEmpty()) {
			top.setBounds(playZone.getWidth() + gap, 0, pipeWidth, topHeight);
			bottom.setBounds(playZone.getWidth() + gap, topHeight + space, pipeWidth, bottomHeight);
		} else {
			top.setBounds(pipes.getLast().getX() + gap, 0, pipeWidth, topHeight);
			bottom.setBounds(pipes.getLast().getX() + gap, topHeight + space, pipeWidth, bottomHeight);
		}
		
		playZone.add(top);
		playZone.add(bottom);
		pipes.addLast(top);
		pipes.addLast(bottom);
		
	}

	public void jump() {
		fallingVelocity = -10;
	}
	private void pipesMotion(){
		for(JButton pipe : pipes){
			pipe.setLocation(pipe.getX()-2, pipe.getY());
			if(pipe.getX()+pipe.getWidth() < 0){
				pipes.removeFirst();
				pipes.removeFirst();
				addPipe();
			}
		}
		
	}
	private void frogMotion() {
		if (fallingVelocity < 10) {
			fallingVelocity += 1;
		}

		frog.setLocation(frog.getX(), frog.getY() + fallingVelocity);
		if (frog.getY() >= playZone.getHeight()-frog.getHeight()) {
			fallingVelocity = 0;
			frog.setLocation(frog.getX(), playZone.getHeight()-frog.getHeight());
		}

	}

	public void g() {
		// Default location of the frog
		frog.setLocation(new Point(playZone.getWidth() / 4 - frog.getWidth() / 2, playZone.getHeight() / 2 - frog.getHeight() / 2));
		frog.setText("");
		System.out.println("djt con me may");
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
