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
	List<JButton> pipes;
	public GameController() {
	}

	public GameController(Game game) {
		this.game = game;
		pipes = new ArrayList<>();
		initInteface();
		playZone.addMouseListener(this);
		addPipe();
//		initPipes();
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
						Thread.sleep(29);
					} catch (Exception e) {
					}
				}
			}
			
		}.start();
	}
	private void initInteface(){
		playZone = new JPanel();
		frog = new JLabel();
		playZone.setLocation(6, 6);
		playZone.setSize(700, 448);
		frog.setIcon(new ImageIcon("./rsz_1dz.jpg"));
		frog.setLocation(playZone.getWidth()/2-frog.getWidth(), playZone.getHeight()/2-frog.getHeight());
		playZone.add(frog);
		game.add(playZone, BorderLayout.CENTER);
	}
	private void initPipes(){
		for (int i = 0; i < 2; i++) {
			addPipe();
		}
	}
	private void addPipe(){
		Random rand = new Random();
		int pipeWidth = 40;
		int space = 140;
		int topHeight = 50 + rand.nextInt(250);
		int bottomHeight = playZone.getHeight()-space-topHeight;
		int pipeCapacity = pipes.size()/2;
		JButton top = new JButton();
		JButton bottom = new JButton();
		playZone.add(top);
		playZone.add(bottom);
		top.setLocation(playZone.getWidth() + pipeCapacity * 170, 0);
		bottom.setLocation(playZone.getWidth() + pipeCapacity * 170, topHeight + space);
		top.setPreferredSize(new Dimension(pipeWidth, topHeight));
		bottom.setPreferredSize(new Dimension(pipeWidth, bottomHeight));
		pipes.add(top);
		pipes.add(bottom);
		
	}

	public void jump() {
		fallingVelocity = -10;
	}
	private void pipesMotion(){
		for(int i = 0; i < pipes.size(); i++){
			pipes.get(i).setLocation(pipes.get(i).getX()-1, pipes.get(i).getY());
			if(pipes.get(i).getX()+pipes.get(i).getWidth() < 0){
				pipes.remove(i--);
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
