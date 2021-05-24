/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.math.BigDecimal;
import java.math.MathContext;
import javax.swing.JButton;
import javax.swing.JTextField;
import view.CalculatorForm;

/**
 *
 * @author hoang
 */
public class CalculatorController {

    private BigDecimal firstNumber, secondNumber, memory;
    private JTextField txtDisplay;
    private boolean isReset = true;
    private boolean isPickingOperator = false;
    private Operator operator;

    public CalculatorController() {
    }

    public CalculatorController(JTextField txtDisplay) {
        this.txtDisplay = txtDisplay;
        this.operator = Operator.NONE;
        this.memory = BigDecimal.ZERO;
    }

    private boolean isZero() {
        return txtDisplay.getText().equals("0");
    }

    private void appendDigit(String value) {
        txtDisplay.setText(txtDisplay.getText() + value);
    }

    private boolean hasMemory() {
        return !memory.equals(BigDecimal.ZERO);
    }

    public void addDigit(JButton btn) {
        if (isZero() || isReset) {
            txtDisplay.setText(btn.getText());
            isReset = false;
        } else {
            appendDigit(btn.getText());
        }
        isPickingOperator = false;
    }

    public void chooseBasicOperator(JButton op) {
        if (operator == Operator.NONE && !txtDisplay.getText().contains("ERROR")) {
            firstNumber = new BigDecimal(txtDisplay.getText());
        } else if (!isPickingOperator) {
            calculate();
        } else {
            firstNumber = BigDecimal.ZERO;
        }

        switch (op.getText()) {
            case "+":
                operator = Operator.ADD;
                break;
            case "-":
                operator = Operator.SUBTRACT;
                break;
            case "x":
                operator = Operator.MULTIPLY;
                break;
            case "/":
                operator = Operator.DIVIDE;
                break;
            default:
                break;
        }
        isPickingOperator = true;
        isReset = true;
    }

    public void chooseFunctionalOperator(JButton btnOperator) {

        if (txtDisplay.getText().contains("ERROR")) {
            firstNumber = BigDecimal.ZERO;
        } else {
            firstNumber = new BigDecimal(txtDisplay.getText());
        }
        switch (btnOperator.getText()) {
            case "+/-":
                firstNumber = firstNumber.negate();
                isReset = false;
                txtDisplay.setText(firstNumber.stripTrailingZeros() + "");
                return;
            case "1/x":
                if (firstNumber.equals(BigDecimal.ZERO)) {
                    txtDisplay.setText("MATH ERROR");
                    operator = Operator.NONE;
                    isReset = true;
                    return;
                }
                firstNumber = BigDecimal.ONE.divide(firstNumber, MathContext.DECIMAL64);
                break;
            case "%":
                firstNumber = firstNumber.divide(BigDecimal.valueOf(100));
                break;
            case "√":
                if (firstNumber.doubleValue() < 0) {
                    txtDisplay.setText("MATH ERROR");
                    operator = Operator.NONE;
                    isReset = true;
                    return;
                }
                firstNumber = BigDecimal.valueOf(Math.sqrt(firstNumber.doubleValue()));
                break;
            default:
                if (firstNumber.doubleValue() < 0) {
                    txtDisplay.setText("ERROR");
                    isReset = true;
                    return;
                }
                firstNumber = BigDecimal.valueOf(Math.sqrt(firstNumber.doubleValue()));
                break;
        }
        this.operator = Operator.NONE;
        isPickingOperator = false;
        isReset = true;
        txtDisplay.setText(firstNumber.stripTrailingZeros() + "");
    }

    public void calculate() {
        if (operator == Operator.NONE) {
            isReset = true;
            return;
        }
        try {
            secondNumber = new BigDecimal(txtDisplay.getText());
        } catch (NumberFormatException e) {
            txtDisplay.setText(firstNumber.doubleValue() + "");
            isReset = true;
            return;
        }

        switch (operator) {
            case ADD:
                firstNumber = firstNumber.add(secondNumber);
                break;
            case SUBTRACT:
                firstNumber = firstNumber.subtract(secondNumber);
                break;
            case MULTIPLY:
                firstNumber = firstNumber.multiply(secondNumber);
                break;
            case DIVIDE:
                if (secondNumber.equals(BigDecimal.ZERO)) {
                    txtDisplay.setText("MATH ERROR");
                    operator = Operator.NONE;
                    isReset = true;
                    return;
                }
                firstNumber = firstNumber.divide(secondNumber, MathContext.DECIMAL64);
                break;
        }
        operator = Operator.NONE;
        isPickingOperator = false;
        isReset = true;
        txtDisplay.setText(firstNumber.stripTrailingZeros() + "");
    }

    public void readMemory() {
        txtDisplay.setText(memory + "");
        isReset = true;
    }

    public void addMemory(JButton btn) {
        memory = memory.add(new BigDecimal(txtDisplay.getText()));
        btn.setEnabled(hasMemory());
        isReset = true;
    }

    public void minusMemory(JButton btn) {
        memory = memory.subtract(new BigDecimal(txtDisplay.getText()));
        btn.setEnabled(hasMemory());
        isReset = true;
    }

    public void clearMemory(JButton btn) {
        memory = BigDecimal.ZERO;
        btn.setEnabled(hasMemory());
    }

    public void saveCurrentNumberToMemory(JButton btn) {
        memory = new BigDecimal(txtDisplay.getText());
        btn.setEnabled(hasMemory());
        isReset = true;
    }

    public void addDecimal() {
        if (txtDisplay.getText().contains(".")) {
            return;
        }
        appendDigit(".");
        isReset = false;
    }

    public void deleteSingleDigit() {
        String currentNumber = txtDisplay.getText();
        int numberLength = currentNumber.length();
        if (currentNumber.startsWith("-") && numberLength == 2) {
            txtDisplay.setText("0");
        } else if (numberLength > 1) {
            currentNumber = currentNumber.substring(0, numberLength - 1);
            txtDisplay.setText(currentNumber);
        } else if (numberLength == 1) {
            txtDisplay.setText("0");
        }
    }

    public void clear() {
        firstNumber = BigDecimal.ZERO;
        secondNumber = BigDecimal.ZERO;
        operator = Operator.NONE;
    }

    public static void main(String[] args) {
        CalculatorForm c = new CalculatorForm();
        c.setLocationRelativeTo(null);
        c.setVisible(true);
    }
}
