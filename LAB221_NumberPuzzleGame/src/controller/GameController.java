package controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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

	public GameController() {
		addAction();
		v.setVisible(true);
	}

	void addAction() {
		btnNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setupGame();
			}
		});

	}
	
	void updateSize() {
		String s = cbbSize.getSelectedItem().toString();
		String[] size = s.split("x");
		this.size = Integer.parseInt(size[0]);
	}
	
	void setupGame(){				
		updateSize();
		panelGameArea.removeAll();
		panelGameArea.setLayout(new GridLayout(size, size, GRID_GAP, GRID_GAP));
		
		for(int i = 0; i < size*size; i++){
			JButton x = new JButton("x");
			x.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
			panelGameArea.add(x);
		}
		
		v.pack();
	}
	void run() {
		updateSize();
	}

	public static void main(String[] args) {
		GameController g = new GameController();

	}
}
