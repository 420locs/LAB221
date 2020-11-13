package controller;

import entity.*;
import game.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Ninh
 */
public class GameController{
	private Frog frog;										
	private LinkedList<Pipe> pipes;
	private GameMemory memory;
	private boolean hasSave = false;
	
	
	private Game game;
//	private JLabel frog;
	private JPanel panelPlayzone;
	private JLabel labelScoreboard;

	private boolean isRunning;
	private boolean hasOver;
	private boolean hasStart;
	private boolean hasPause;
	private int score;

	public GameController() {
	}

	public GameController(Game game) {
		this.game = game;
		pipes = new LinkedList<>();
		score = 0;
		initInteface();
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
	
	//<editor-fold defaultstate="collapsed" desc=" Getter and Setter ">
	
	public boolean hasPause() {
		return hasPause;
	}

	public void setPause(boolean hasPause) {
		this.hasPause = hasPause;
	}

	public boolean hasSave() {
		return hasSave;
	}
	
	public void setSave(boolean hasSave) {
		this.hasSave = hasSave;
	}

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
	//</editor-fold>
	
	/**
	 * Initialize all game graphics components.
	 * <ul>
	 *	<li>New Scoreboard</li>
	 *	<li>New Frog</li>
	 *	<li>New Pipes</li>
	 * </ul>
	 */
	private void initInteface(){
		labelScoreboard = game.getLabelScoreboard();
		labelScoreboard.setText("Points: 0");
		panelPlayzone = game.getPanelPlayzone();
		panelPlayzone.removeAll();
		frog = new Frog(new ImageIcon("./frog.jpg"),panelPlayzone);
		initPipes();
		panelPlayzone.updateUI();
	}
	
	/**
	 * Initialize 5 pipe rounding in the game.
	 */
	private void initPipes(){
		for (int i = 0; i < 5; i++) {
			addPipe();
		}
	}
	/**
	 * Add a Pipe to the Play zone.
	 * Add new pipe into LinkedList of pipes 
	 * and set both top and bottom column the action event jump of the Frog.
	 */
	private void addPipe() {
		Pipe pipe = new Pipe();
		pipe = pipe.generatePipe(panelPlayzone, pipes);
		pipe.getTop().addActionListener((ActionEvent e) -> {
			jump();
		});
		pipe.getBottom().addActionListener((ActionEvent e) -> {
			jump();
		});
		pipes.add(pipe);
	}

	/**
	 * Action the motion of pipes in the game.
	 * using a for loop to apply the change of position to every single pipe.
	 * if the Frog touch the Pipe -> pipe stop moving, frog cant jump.
	 * if the Frog pass any pipe, set the hasCount of pipe -> true to ignore the count later.
	 * if any Pipe run out of the screen -> create another one after the last pipe.
	 */
	private void pipesMotion(){
		for(Pipe pipe : pipes){
			pipe.move(2);
			int location = pipe.getTop().getX();
			// When the Frog touch the Pipe
			if (frog.hasIntersect(pipe)) {
				isRunning = false;
				hasOver = true;
				return;
			}
			// When the location of pipe start "behind" the frog position
 			if(location <= panelPlayzone.getWidth()/3 && !pipe.hasCount()){
				labelScoreboard.setText("Points: " + ++score);
				pipe.setCount(true);
			}
			// When the pipe run out of the screen
			if(location + pipe.WIDTH < 0){
				pipes.removeFirst();
				addPipe();
			}
		}
		
	}
	
	/**
	 * Action jump of the Frog.
	 * to start game if game doesn't start yet
	 * able to jump until game over.
	 */
	public void jump() {
		if (!hasPause) {
			if (!hasStart) {
				hasStart = true;
			}
			if (!hasOver) {
				isRunning = true;
				frog.setFallingVelocity(-10);
			}
		}
	}
	
	/**
	 * Action the motion of the frog.
	 * motion : falling with max velocity = 12
	 * block the top of the play zone.
	 * if the Frog lands -> game over.
	 */
	private void frogMotion() {
		// Increase the falling velocity and block it, max = 12
		if (frog.getFallingVelocity() < 12) {
			frog.setFallingVelocity(frog.getFallingVelocity()+1);
		}

		frog.fall();
		
		// Block top
		if(frog.getY() <= 0){
			frog.setFallingVelocity(0);
			frog.setLocation(frog.getX(),0);
		}
		// Block land
		if (frog.getY() >= panelPlayzone.getHeight()-frog.getHeight()) {
			frog.setFallingVelocity(0);
			frog.setLocation(frog.getX(), panelPlayzone.getHeight()-frog.getHeight());
			isRunning = false;
			hasStart = false;
			gameOver();
		}
	}
	
	/**
	 * Action the game over event.
	 * push a gameover popup show the score.
	 * start a new game after that.
	 */
	private void gameOver(){
		hasOver = true;
		JOptionPane.showMessageDialog(panelPlayzone, "Score: " + score, "GAMEOVER", JOptionPane.PLAIN_MESSAGE);
		newGame();
	}
	
	/**
	 * Action the new game event.
	 * reset point to 0.
	 * clear all old pipes.
	 * create new interface of the game. 
	 */
	private void newGame(){
		hasOver = false;
		score = 0;
		pipes.clear();
		initInteface();
		defaultState();
	}
	
	/**
	 * Reset default state for new game.
	 */
	private void defaultState(){
		isRunning = false;
		hasOver = false;
		hasStart = false;
	}
	
	/**
	 * Save current game infomations.
	 * <h6>Infomation:</h6>
	 * <ul>
	 *	<li>Frog's position</li>
	 *	<li>Pipes' position and size</li>
	 *	<li>Game's state</li>
	 *	<li>Score</li>
	 * </ul>
	 */
	public void saveGame(){
		memory = new GameMemory(pipes, frog, score, hasStart, hasOver, isRunning);
		hasSave = true;
	}
	
	/**
	 * Turn back the saved game.
	 * get information from the Memory and restore game.
	 * clear game panel and add new object from Memory.
	 */
	public void returnGame(){
		panelPlayzone.removeAll();
		frog = new Frog(new ImageIcon("./frog.jpg"),panelPlayzone);
		frog.setLocation(memory.getFrogX(), memory.getFrogY());
		frog.setFallingVelocity(memory.getFrogVelocity());
		pipes.clear();
		pipes = memory.getPipes();
		for(Pipe pipe : pipes){
			panelPlayzone.add(pipe.getTop());
			panelPlayzone.add(pipe.getBottom());
		}
		hasOver = memory.hasOver();
		hasStart = memory.hasStart();
		isRunning = memory.isRunning();
		score = memory.getScore();
		labelScoreboard.setText("Points: " + score);
		panelPlayzone.updateUI();
		hasSave = false;
	}
	

}
