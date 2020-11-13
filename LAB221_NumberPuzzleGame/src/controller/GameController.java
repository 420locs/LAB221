package controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import view.GameUI;

/**
 *
 * @author Ninh
 */
public class GameController {
	final int BUTTON_SIZE = 60;
	final int GRID_GAP = 20;
	
	private final GameUI v = new GameUI();
	private final JButton btnNewGame = v.getBtnNewGame();
	private final JLabel labelElapsed = v.getLabelElapsed();
	private final JLabel labelMoveCount = v.getLabelMoveCount();
	private final JComboBox cbbSize = v.getCbbSize();
	private final JPanel panelGameArea = v.getPanelGameArea();

	private int size = 3; //default size is 3x3
	private final List<JButton> buttonMap = new ArrayList<>();
	private List<Integer> gameVector;
	private int emptyButtonIndex;
	private int timer;
	private int moveCount;
	private boolean isPlaying = false;
	

	public GameController() {
		addAction();
		Timer t = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isPlaying) {
					timer++;
				}
				labelElapsed.setText(timer + " sec");
			}
		});
		t.start();
//		Thread timing = new Thread(){
//			@Override
//			public void run() {
//				while(true){
//					try {
//						Thread.sleep(1);
//						if (isPlaying) {
//							timer++;
//						}
//						labelElapsed.setText(timer + " sec");
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//					}
//				}
//			}	
//		};
//		timing.start();
		v.setVisible(true);
	}

	/**
	 * Create base-buttons of the game.
	 * create "New Game" button
	 */
	void addAction() {
		btnNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isPlaying){
					int choice = JOptionPane.showConfirmDialog(v, "Hey, you just playing another game\nWanna create new game?", "Continue?", JOptionPane.YES_NO_OPTION);
					if(choice == JOptionPane.NO_OPTION)
						return;
					isPlaying = false;
				}
				/*
				Some fetures of gameplay here
				*/
				timer = 0;
				moveCount = 0;			
				labelMoveCount.setText(moveCount + "");
				setupGame();
			}
		});
		
	}
	
	/**
	 * Update size of game.
	 * get size from ComboBox and assign to global size.
	 */
	void updateSize() {
		String s = cbbSize.getSelectedItem().toString();
		String[] sizeOfGame = s.split("x");
		this.size = Integer.parseInt(sizeOfGame[0]);
	}
	
	/**
	 * Generate solvable game vector.
	 * initialize an ArrayList contains number from 1 to size*size,
	 * shuffles it until found an solvable game vector.
	 * @return solvable game vector.
	 */
	List<Integer> generateGameVector(){
		List<Integer> list = new ArrayList<>();
		for(int i = 0; i < size*size; i++){
			list.add(i+1);
		}
		do {
			Collections.shuffle(list);
		} while (!isSolvable(list));
		return list;
	}
	
	/**
	 * Check can a game is solvable or not.
	 * A solvable game must have 2 factors:
	 *	+ Inversion: pair of numbers haven't in order (number a > number b)
	 *	+ Polarity: number of Inversion 
	 *		If size is odd -> polarity : Odd is unsolvable, Even is solvable
	 *		If size is even:
	 *			Blank button in odd row(counting from the top) -> polarity : Odd is solvable, Even is unsolvable
	 *			otherwise -> polarity : Odd is unsolvable, Even is solvable
	 * @param list list of number shuffled
	 * @return True solvable list; False otherwise
	 */
	boolean isSolvable(List<Integer> list){
		int polarity = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == size * size) {
				continue;
			}
			for (int j = i; j < list.size(); j++) {
				if (list.get(i) > list.get(j)) {
					polarity++;
				}
			}
		}
		
		if (size % 2 == 1) {
			return polarity % 2 == 0;
		} else {
			if (isBlankInOddRow(list)) {
				return polarity % 2 == 1;
			} else {
				return polarity % 2 == 0;
			}
		}
	}
	/**
	 * Check the position of blank button is in odd row 
	 * @param list vector of game
	 * @return True in odd row; False otherwise
	 */
	private boolean isBlankInOddRow(List<Integer> list) {
		for (int i = 0; i < size * size; i++) {
				if(list.get(i) == size*size)
					return true;
				if (i % size == size - 1) {
					i += size;
				}
			}
		return false;
	}
	 
	/**
	 * Setup game layout.
	 * add buttons and resize game layout.
	 */
	void setupGame(){				
		updateSize();
		panelGameArea.removeAll();
		panelGameArea.setLayout(new GridLayout(size, size, GRID_GAP, GRID_GAP));
		gameVector = generateGameVector();
		
		for(int i = 0; i < size*size; i++){
			int number = gameVector.get(i);
			JButton button = new JButton();
			button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
			addActionButton(button);
			if(number == size*size){ // set empty label to last key of buttonMap
				button.setText("");
				emptyButtonIndex = i; // set default Empty slot
			}
			else 
				button.setText(number+"");
			
			panelGameArea.add(button);
			buttonMap.add(i, button);
		}
		
		v.pack();
	}
	
	/**
	 * Add Action listener to Button.
	 * add moving feature to any button
	 * @param button 
	 */
	void addActionButton(JButton button){
		button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(button.getText().isEmpty())
						return;
					if(!isPlaying){
						isPlaying = true;
					}
					int currentBtn = Integer.parseInt(button.getText());
					int currentIndex = gameVector.indexOf(currentBtn);
					moving(currentIndex);
					winningNotification();
				}
			});
	}
	
	void moving(int current){
		if(hasNear(current)){ //If clicked button near by empty button -> swap
			swapButton(current, emptyButtonIndex);
			emptyButtonIndex = current;
			labelMoveCount.setText(++moveCount + "");
		}
	}
	boolean hasNear(int current){
		return isTop(current) || isBottom(current) || isLeft(current) || isRight(current);
	}
	boolean isTop(int current){
		return current-size == emptyButtonIndex;
	}
	boolean isBottom(int current){
		return current+size == emptyButtonIndex;
	}
	boolean isLeft(int current){
		return current-1 == emptyButtonIndex;
	}
	boolean isRight(int current){
		return current+1 == emptyButtonIndex;
	}
	
	/**
	 * Swap any 2 button with their index.
	 * @param indexA 
	 * @param indexB 
	 */
	void swapButton(int indexA, int indexB){
		String tempS = buttonMap.get(indexA).getText();
		buttonMap.get(indexA).setText(buttonMap.get(indexB).getText());
		buttonMap.get(indexB).setText(tempS);
		
		int tempN = gameVector.get(indexA);
		gameVector.set(indexA, gameVector.get(indexB));
		gameVector.set(indexB, tempN);
		
	}
	void winningNotification(){
		if(!isWon())
			return;
		isPlaying = false;
		JOptionPane.showMessageDialog(v, "Hey, you just won!");
	}
	/**
	 * Are you winning, son?.
	 * check matched key-value of every button from buttonMap
	 * @return Winning status
	 */
	boolean isWon(){
		for(int i = 0; i < gameVector.size()-1; i++){
			if(gameVector.get(i) > gameVector.get(i+1))
				return false;
		}
		return true;
	}
	
}
