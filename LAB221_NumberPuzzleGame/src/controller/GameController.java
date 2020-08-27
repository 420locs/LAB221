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
	private List<Integer> gameMatrix;
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
		String[] size = s.split("x");
		this.size = Integer.parseInt(size[0]);
	}
	
	List<Integer> generateRandomMatrixNumber(){
		List<Integer> list = new ArrayList<>();
		for(int i = 0; i < size*size; i++){
			list.add(i+1);
		}
		Collections.shuffle(list);
		return list;
	}

	 
	
	void setupGame(){				
		updateSize();
		panelGameArea.removeAll();
		panelGameArea.setLayout(new GridLayout(size, size, GRID_GAP, GRID_GAP));
		gameMatrix = generateRandomMatrixNumber();
		for(int i = 0; i < size*size; i++){
			int number = gameMatrix.get(i);
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
					int current = gameMatrix.indexOf(currentBtn);
					moving(current);
					monitoringGameMatrix();
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
		if(isTop(current) || isBottom(current) || isLeft(current) || isRight(current))
			return true;
		return false;
	}
	boolean isTop(int current){
		if(current-size == emptyButtonIndex)
			return true;
		return false;
	}
	boolean isBottom(int current){
		if(current+size == emptyButtonIndex)
			return true;
		return false;
	}
	boolean isLeft(int current){
		if(current-1 == emptyButtonIndex)
			return true;
		return false;
	}
	boolean isRight(int current){
		if(current+1 == emptyButtonIndex)
			return true;
		return false;
	}
	
	void swapButton(int indexA, int indexB){
		indexA++; // Becase we swap 2 key from buttonMap (start from 1)
		indexB++; // And array start from 0. So we increase 1 for compatible manipulation
		String tempS = buttonMap.get(indexA).getText();
		buttonMap.get(indexA).setText(buttonMap.get(indexB).getText());
		buttonMap.get(indexB).setText(tempS);
		
		int tempN = gameMatrix.get(--indexA);
		gameMatrix.set(indexA, gameMatrix.get(--indexB));
		gameMatrix.set(indexB, tempN);
		
	}
	void winningNotification(){
		if(!isWin())
			return;
		JOptionPane.showMessageDialog(labelMoveCount, "Hey, you just won!");
	}
	/**
	 * Are you winning, son?
	 * check matched key-value of every button from buttonMap
	 * @return Winning status
	 */
	boolean isWin(){
		for(Map.Entry<Integer, JButton> entry : buttonMap.entrySet()){
			int key = entry.getKey();
			String text = entry.getValue().getText();
			int value = size*size;
			try {
				value = Integer.parseInt(text);
			} catch (Exception e) {
			}
//			System.out.println(key + " : " + value); debugger :))))
			if(key != value)
				return false;
		}
		return true;
	}
	
	private void monitoringGameMatrix() {
		for (int i = 0; i < size*size; i++) {
			if(i % size == 0)
				System.out.println("");
			System.out.print(gameMatrix.get(i) + " ");
		}
	}
	
	public static void main(String[] args) {
		GameController g = new GameController();

	}
}
