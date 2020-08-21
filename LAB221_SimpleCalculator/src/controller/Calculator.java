package controller;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Ninh
 */
enum Operator {
	NONE, ADD, SUBTRACT, MULTIPLY, DIVIDE
}

public class Calculator {
	private double number0, number1, memory = 0;
	
	private JTextField txtDisplay;
	private boolean isReset = true;
	private boolean isPickingOperator = false;
	private Operator operator = Operator.NONE;
		
	public Calculator() {
	}

	public Calculator(JTextField txtDisplay) {
		this.txtDisplay = txtDisplay;
	}
	
	private boolean isZero(){
		if(txtDisplay.getText().equals("0") || txtDisplay.getText().equals("0.0"))
			return true;
		return false;
	}
	private void displayAppend(String value){
		txtDisplay.setText(txtDisplay.getText() + value);
	}
	private boolean hasMemory(){
		if(memory == 0)
			return false;
		return true;
	}
	public void pressDigit(JButton btn){
		if(isZero() || isReset) {
			txtDisplay.setText(btn.getText());
			isReset = false;
		} else {
			displayAppend(btn.getText());
		}
	}
	public void pressOperator(JButton op){
		if(operator == Operator.NONE){
			number0 = Double.parseDouble(txtDisplay.getText());
		} else {
			if(!isPickingOperator)
				pressEqual();
		}
		
		switch(op.getText().trim()){
			case "+":
				operator = Operator.ADD;
				break;
			case "-":
				operator = Operator.SUBTRACT;
				break;
			case "x":
				operator = Operator.MULTIPLY;
				break;
			default:
				operator = Operator.DIVIDE;
				break;
		}
		isPickingOperator = true;
		isReset = true;
	}
	public void pressSingleOperator(JButton operator){
		if(isZero())
			return;

		number0 = Double.parseDouble(txtDisplay.getText());
		switch(operator.getText()){
			case "+/-":
				number0 = -number0;
				break;
			case "1/x":
				number0 = 1/number0;
				break;
			case "%":
				number0 = number0/100;
				break;
			default:
				if(number0 < 0){
					JOptionPane.showMessageDialog(null, "You cannot square to negative number!");
					break;
				}
				number0 = Math.sqrt(number0);
				break;
		}
		txtDisplay.setText(number0+"");
		isReset = true;
	}
	public void pressEqual(){
		if(operator == Operator.NONE){
			isReset = true;
			return;
		}
		try {
			number1 = Double.parseDouble(txtDisplay.getText());
		} catch (NumberFormatException e) {
			txtDisplay.setText(number0+"");
			isReset = true;
			return;
		}
		
		switch(operator){
			case ADD:
				number0 += number1;
				break;
			case SUBTRACT:
				number0 -= number1;
				break;
			case MULTIPLY:
				number0 *= number1;
				break;
			case DIVIDE:
				if(number1 == 0){
					JOptionPane.showMessageDialog(null, "You cannot divide by zero!");
					break;
				}
				number0 /= number1;
				break;
		}
		operator = Operator.NONE;
		txtDisplay.setText(number0+"");
		isPickingOperator = false;
		isReset = true;
	}
	public void pressMR(){
		txtDisplay.setText(memory+"");
		isReset = true;
	}
	public void pressMAdd(JButton btnMR){
		memory += Double.parseDouble(txtDisplay.getText());
		btnMR.setEnabled(hasMemory());
		isReset = true;
	}
	public void pressMMinus(JButton btnMR){
		memory -= Double.parseDouble(txtDisplay.getText());
		btnMR.setEnabled(hasMemory());
		isReset = true;
	}
	public void pressMC(JButton btnMR){
		memory = 0;
		btnMR.setEnabled(hasMemory());
	}
	public void pressClearAll(){
		number0 = 0;
		number1 = 0;
	}
}
