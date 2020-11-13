package entity;

import java.util.LinkedList;

/**
 *
 * @author Ninh
 */
public class GameMemory {
	private LinkedList<Pipe> pipes = new LinkedList<>();
	private int frogX, frogY, frogVelocity;
	private int score;
	private boolean hasStart;
	private boolean hasOver;
	private boolean isRunning;

	public GameMemory() {
	}

	public GameMemory(LinkedList<Pipe> pipeList, Frog frog, int score, boolean hasStart, boolean hasOver, boolean isRunning) {
		pipes = new LinkedList<>();
		for(Pipe p : pipeList){
			pipes.add(new Pipe(p.getLocationX(), p.getTopHeight(), p.getBottomHeight()));
		}
		this.frogX = frog.getX();
		this.frogY = frog.getY();
		this.frogVelocity = frog.getFallingVelocity();
		this.score = score;
		this.hasStart = hasStart;
		this.hasOver = hasOver;
		this.isRunning = isRunning;
	}

	public LinkedList<Pipe> getPipes() {
		return pipes;
	}

	public int getFrogX() {
		return frogX;
	}

	public int getFrogY() {
		return frogY;
	}

	public int getFrogVelocity() {
		return frogVelocity;
	}

	public int getScore() {
		return score;
	}

	public boolean hasStart() {
		return hasStart;
	}

	public boolean hasOver() {
		return hasOver;
	}

	public boolean isRunning() {
		return isRunning;
	}
	
	

	
	
}
