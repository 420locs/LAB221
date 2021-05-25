package controller;

import java.math.BigDecimal;
import java.math.MathContext;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 *
 * @author Ninh
 */
enum Operator {
    NONE, ADD, SUBTRACT, MULTIPLY, DIVIDE
}

public class Calculator {

    private BigDecimal number0, number1, memory = BigDecimal.ZERO;

    private JTextField txtDisplay;
    private boolean isReset = true;
    private boolean isPickingOperator = false;
    private Operator operator = Operator.NONE;

    public Calculator() {
    }

    public Calculator(JTextField txtDisplay) {
        this.txtDisplay = txtDisplay;
    }

    private boolean isZero() {
        return txtDisplay.getText().equals("0") || txtDisplay.getText().equals("0.0");
    }

    private void displayAppend(String value) {
        txtDisplay.setText(txtDisplay.getText() + value);
    }

    private boolean hasMemory() {
        return memory != BigDecimal.ZERO;

    }

    public void pressDigit(JButton btn) {
        if (isZero() || isReset) {
            txtDisplay.setText(btn.getText());
            isReset = false;
        } else {
            displayAppend(btn.getText());
        }
        isPickingOperator = false;
    }

    public void pressOperator(JButton op) {
        if (operator == Operator.NONE) {
            number0 = new BigDecimal(txtDisplay.getText());
        } else {
            if (!isPickingOperator) {
                pressEqual();
            }
        }

        switch (op.getText().trim()) {
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

    public void pressSingleOperator(JButton operator) {
        if (isZero()) {
            return;
        }

        number0 = new BigDecimal(txtDisplay.getText());
        switch (operator.getText()) {
            case "+/-":
                number0 = number0.negate();
                break;
            case "1/x":
                number0 = BigDecimal.ONE.divide(number0);
                break;
            case "%":
                number0 = number0.divide(BigDecimal.valueOf(100));
                break;
            default:
                if (number0.doubleValue() < 0) {
                    txtDisplay.setText("ERROR");
                    isReset = true;
                    return;
                }
                number0 = BigDecimal.valueOf(Math.sqrt(number0.doubleValue()));
                break;
        }
        this.operator = Operator.NONE;
        isPickingOperator = false;
        isReset = true;
        txtDisplay.setText(number0.doubleValue() + "");
    }

    public void pressEqual() {
        if (operator == Operator.NONE) {
            isReset = true;
            return;
        }
        try {
            number1 = new BigDecimal(txtDisplay.getText());
        } catch (NumberFormatException e) {
            txtDisplay.setText(number0.doubleValue() + "");
            isReset = true;
            return;
        }

        switch (operator) {
            case ADD:
                number0 = number0.add(number1);
                break;
            case SUBTRACT:
                number0 = number0.subtract(number1);
                break;
            case MULTIPLY:
                number0 = number0.multiply(number1);
                break;
            case DIVIDE:
                if (number1 == BigDecimal.ZERO) {
                    txtDisplay.setText("ERROR");
                    operator = Operator.NONE;
                    isReset = true;
                    return;
                }
                number0 = number0.divide(number1, MathContext.DECIMAL64);
                break;
        }
        operator = Operator.NONE;
        isPickingOperator = false;
        isReset = true;
        txtDisplay.setText(number0.doubleValue() + "");
    }

    public void pressMR() {
        txtDisplay.setText(memory + "");
        isReset = true;
    }

    public void pressMAdd(JButton btnMR) {
        memory.add(new BigDecimal(txtDisplay.getText()));
        btnMR.setEnabled(hasMemory());
        isReset = true;
    }

    public void pressMMinus(JButton btnMR) {
        memory.subtract(new BigDecimal(txtDisplay.getText()));
        btnMR.setEnabled(hasMemory());
        isReset = true;
    }

    public void pressMC(JButton btnMR) {
        memory = BigDecimal.ZERO;
        btnMR.setEnabled(hasMemory());
    }

    public void pressDot() {
        if (txtDisplay.getText().contains(".")) {
            return;
        }

        displayAppend(".");
    }

    public void pressClearAll() {
        number0 = BigDecimal.ZERO;
        number1 = BigDecimal.ZERO;
        operator = Operator.NONE;
    }
}
