package controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	private final HashMap<Integer, JButton> buttonMap = new HashMap<>();
	private List<Integer> gameVector;
	private int emptyButtonIndex;
	private int moveCount;
	private int elapsed;


	public GameController() {
		addAction();
		v.setVisible(true);
	}

	void addAction() {
		btnNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				Some fetures of gameplay here
				*/
				setupGame();
			}
		});

	}
	
	void updateSize() {
		String s = cbbSize.getSelectedItem().toString();
		String[] sizeOfGame = s.split("x");
		this.size = Integer.parseInt(sizeOfGame[0]);
	}
	
	List<Integer> generateGameVector(){
		List<Integer> list = new ArrayList<>();
		for(int i = 0; i < size*size; i++){
			list.add(i+1);
		}
		do {
			Collections.shuffle(list);
		} while (!canSolved(list));
		return list;
	}
	/**
	 * Check can a game is solvable or not.
	 * A solvable game must have 2 factors:
	 *	+ Inversion: pair of numbers haven't in order (number a > number b)
	 *	+ Polarity: number of Inversion (Odd is unsolvable, Even is solvable)
	 * @param list of number shuffled
	 * @return solvable or not
	 */
	boolean canSolved(List<Integer> list){
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
		if(polarity % 2 == 1 )
			System.out.println("oops");
		return polarity % 2 == 0;
	}
	 
	
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
			buttonMap.put(i+1, button);
		}
		
		v.pack();
	}
	void addActionButton(JButton button){
		button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(button.getText().isEmpty())
						return;
					int currentBtn = Integer.parseInt(button.getText());
					int current = gameVector.indexOf(currentBtn);
					moving(current);
					
					winningNotification();
				}
			});
	}
	
	void moving(int current){
		if(hasNear(current)){
			swapButton(current, emptyButtonIndex);
			emptyButtonIndex = current;
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
	
	void swapButton(int indexA, int indexB){
		indexA++; // Becase we swap 2 key from buttonMap (start from 1)
		indexB++; // And array start from 0. So we increase 1 for compatible manipulation
		String tempS = buttonMap.get(indexA).getText();
		buttonMap.get(indexA).setText(buttonMap.get(indexB).getText());
		buttonMap.get(indexB).setText(tempS);
		
		int tempN = gameVector.get(--indexA);
		gameVector.set(indexA, gameVector.get(--indexB));
		gameVector.set(indexB, tempN);
		
	}
	void winningNotification(){
		if(!isWon())
			return;
		JOptionPane.showMessageDialog(v, "Hey, you just won!");
	}
	/**
	 * Are you winning, son?
	 * check matched key-value of every button from buttonMap
	 * @return Winning status
	 */
	boolean isWon(){
		for(Map.Entry<Integer, JButton> entry : buttonMap.entrySet()){
			int key = entry.getKey();
			String text = entry.getValue().getText();
			int value = size*size;
			try {
				value = Integer.parseInt(text);
			} catch (NumberFormatException e) {
			}
//			System.out.println(key + " : " + value); debugger :))))
			if(key != value)
				return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		GameController g = new GameController();

	}
}
