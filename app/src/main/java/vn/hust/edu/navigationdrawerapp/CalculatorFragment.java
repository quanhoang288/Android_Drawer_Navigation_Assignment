package vn.hust.edu.navigationdrawerapp;

import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CalculatorFragment extends Fragment {

    private View rootView;

    private double result = 0;
    private String operator = null;
    private double firstOperand = 0;
    private double secondOperand;
    private boolean hasSecondOperand = false;
    private boolean finishCalculation = false;

    private static final String ADD_OPERATION = "add";
    private static final String SUBTRACT_OPERATION = "subtract";
    private static final String MULTIPLY_OPERATION = "multiply";
    private static final String DIVIDE_OPERATION = "divide";

    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calculator, container, false);

        // init operand value
        TextView operandValText = rootView.findViewById(R.id.operandValue);
        operandValText.setText("0");

        int[] numberButtonIds = {
            R.id.zeroButton,
            R.id.oneButton,
            R.id.twoButton,
            R.id.threeButton,
            R.id.fourButton,
            R.id.fiveButton,
            R.id.sixButton,
            R.id.sevenButton,
            R.id.eightButton,
            R.id.nineButton
        };

        int[] clearButtonIds = {
            R.id.clearAllButton,
            R.id.clearOperandButton,
            R.id.backspaceButton,
        };

        int[] operatorIds = {
            R.id.modularButton,
            R.id.multiplyButton,
            R.id.divideButton,
            R.id.subtractButton,
            R.id.addButton
        };

        for (int numId : numberButtonIds) {
            AppCompatButton btn = rootView.findViewById(numId);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processNumber(view);
                }
            });
        }

        for (int operatorId: operatorIds) {
            AppCompatButton btn = rootView.findViewById(operatorId);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processOperator(view);
                }
            });
        }

        for (int clearId: clearButtonIds) {
            View clearView = rootView.findViewById(clearId);
            clearView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    performClearOperation(view);
                }
            });
        }

        AppCompatButton togglePosAndNegButton = rootView.findViewById(R.id.togglePosAndNegButton);
        AppCompatButton dotButton = rootView.findViewById(R.id.dotButton);
        AppCompatButton equalButton = rootView.findViewById(R.id.equalButton);

        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleEqualButtonClick(view);
            }
        });
        togglePosAndNegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePosAndNeg();
            }
        });
        dotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processDot(view);
            }
        });


        return rootView;
    }

    public void processNumber(View view) {
        AppCompatButton numBtn = (AppCompatButton) view;
        String numVal = numBtn.getText().toString();

        if (this.operator == null) {
            if (this.firstOperand == 0) {
                this.firstOperand = Double.parseDouble(numVal);
            }
            else {
                String firstOperandText = this._operandToText(this.firstOperand);
                this.firstOperand = Double.parseDouble(firstOperandText + numVal);
            }
        } else if (!this.hasSecondOperand || this.secondOperand == 0){
            this.secondOperand = Double.parseDouble(numVal);
        } else {
            String secondOperandText = this._operandToText(this.secondOperand);
            this.secondOperand = Double.parseDouble(secondOperandText + numVal);
        }

        if (this.operator != null) {
            this.hasSecondOperand = true;
        }

        this._updateOperandValueText();
    }

    public void processOperator(View view) {
        AppCompatButton opBtn = (AppCompatButton) view;

        if (this._isReadyToPerformOperation()) {
            try {
                this.result = this._calculate();
                this.firstOperand = this.result;
                this.hasSecondOperand = false;
            } catch (ArithmeticException e) {
                Log.e("divisionByZero", e.getMessage());
            }
        }

        switch (opBtn.getId()) {
            case R.id.addButton:
                this.operator = ADD_OPERATION;
                break;

            case R.id.subtractButton:
                this.operator = SUBTRACT_OPERATION;
                break;

            case R.id.multiplyButton:
                this.operator = MULTIPLY_OPERATION;
                break;

            case R.id.divideButton:
                this.operator = DIVIDE_OPERATION;
                break;

            case R.id.modularButton:
                //TODO: modular operation
                return;

            default:
                return;
        }

        this.finishCalculation = false;

        this._updateOperandValueText();
        this._updateOperationText();
    }

    public void performClearOperation(View view) {

        switch (view.getId()) {
            case R.id.clearOperandButton:
                this._clearOperand();
                break;

            case R.id.clearAllButton:
                this._clearAll();
                break;

            case R.id.backspaceButton:
                this._clearChar();
                break;

            default:
                return;
        }
    }


    public void togglePosAndNeg() {
        if (this.hasSecondOperand) {
            this.secondOperand *= -1;
        } else if (this.operator != null) {
            this.secondOperand = this.firstOperand * -1;
            this.hasSecondOperand = true;
        } else {
            this.firstOperand *= -1;
        }
        this._updateOperandValueText();
    }

    public void processDot(View view) {
        //TODO: decimal point operations
    }

    public void handleEqualButtonClick(View view) {
        this.result = this._calculate();
        this.finishCalculation = true;
        this._updateOperationText();
        this.operator = null;
        this.hasSecondOperand = false;
        this.firstOperand = this.result;
        this._updateOperandValueText();
    }

    private void _initOperandValue() {
        TextView operandValText = rootView.findViewById(R.id.operandValue);
        operandValText.setText("0");
    }

    private boolean _isInteger(double val) {
        return val % 1 == 0;
    }

    private String _operandToText(double operand) {
        String operandText = String.valueOf(operand);

        if (this._isInteger(operand)) {
            return operandText.substring(0, operandText.indexOf("."));
        }
        return operandText;
    }

    private String _formatOperand(double operand) {
        StringBuilder res = new StringBuilder("");

        String operandText = this._operandToText(operand);

        String integerPart = operandText;
        String floatingPart = null;

        boolean hasNegativeSign = operandText.charAt(0) == '-';
        int decimalPointIdx = operandText.indexOf(".");

        if (decimalPointIdx != -1) {
            integerPart = operandText.substring(0, decimalPointIdx);
            floatingPart = operandText.substring(decimalPointIdx + 1);
        }

        ArrayList<Integer> splitIdxes = new ArrayList<Integer>();
        for (int i = integerPart.length() - 3; i > (hasNegativeSign ? 1 : 0); i -= 3) {
            splitIdxes.add(i);
        }

        if (splitIdxes.size() == 0) {
            res.append(integerPart);
        } else {
            int prevIdx = 0;
            for (int i = splitIdxes.size() - 1; i >= 0; i--) {
                int curIdx = splitIdxes.get(i);
                Log.i("current_index", String.valueOf(curIdx));
                res.append(integerPart.substring(prevIdx, curIdx) + ',');
                prevIdx = curIdx;
                if (i == 0) {
                    res.append(integerPart.substring(curIdx));
                }
            }
        }
        if (floatingPart != null) {
            res.append("." + floatingPart);
        }
        return res.toString();
    }

    private String _getOperatorSymbol(String operator) {
        switch (operator) {
            case ADD_OPERATION:
                return "+";

            case SUBTRACT_OPERATION:
                return "-";

            case MULTIPLY_OPERATION:
                return "x";

            case DIVIDE_OPERATION:
                return "/";

            default:
                return null;
        }
    }

    private boolean _isReadyToPerformOperation() {
        return this.hasSecondOperand && this.operator != null;
    }

    private void _updateOperationText() {
        TextView operationText = rootView.findViewById(R.id.operationText);
        StringBuilder operation = new StringBuilder("");
        operation.append(this._operandToText(this.firstOperand) + ' ' + this._getOperatorSymbol(this.operator) + ' ');
        if (this.hasSecondOperand) {
            operation.append(this._operandToText(this.secondOperand) + " = ");
        }
        operationText.setText(operation.toString());
    }

    private void _resetOperationText() {
        TextView operationText = rootView.findViewById(R.id.operationText);
        operationText.setText("");
    }

    private void _updateOperandValueText() {
        TextView operandValueText = rootView.findViewById(R.id.operandValue);
        double operand = this.hasSecondOperand ? this.secondOperand : this.firstOperand;
        operandValueText.setText(this._formatOperand(operand));
    }


    private double _calculate() throws ArithmeticException {
        double res = 0;
        switch (this.operator) {
            case ADD_OPERATION:
                res = this.firstOperand + this.secondOperand;
                break;

            case SUBTRACT_OPERATION:
                res = this.firstOperand - this.secondOperand;
                break;

            case MULTIPLY_OPERATION:
                res = this.firstOperand * this.secondOperand;
                break;

            case DIVIDE_OPERATION:
                if (this.secondOperand == 0) {
                    throw new ArithmeticException();
                }
                res = this.firstOperand / this.secondOperand;
        }
        return res;
    }

    private void _clearAll() {
        this.firstOperand = 0;
        this.hasSecondOperand = false;
        this.operator = null;
        this.result = 0;
        this._resetOperationText();
        this._updateOperandValueText();
    }

    private void _clearChar() {
        if (this.finishCalculation) {
            this.hasSecondOperand = false;
            this.operator = null;
            this._resetOperationText();
            return;
        }
        if (this.operator != null && !this.hasSecondOperand) {
            return;
        }
        double operand = this.hasSecondOperand ? this.secondOperand : this.firstOperand;
//        Log.i("remove", String.valueOf(operand));
        String operandText = this._operandToText(operand);
        StringBuilder slicedOperandText = new StringBuilder(operandText.substring(0, operandText.length() - 1));
        if (slicedOperandText.length() == 0) {
            slicedOperandText = new StringBuilder("0");
        }
        if (this.hasSecondOperand) {
            this.secondOperand = Double.parseDouble(slicedOperandText.toString());
        } else {
            this.firstOperand = Double.parseDouble(slicedOperandText.toString());
        }
        this._updateOperandValueText();
    }

    private void _clearOperand() {
        if (this.finishCalculation) {
            this._clearAll();
            return;
        }
        if (!this.hasSecondOperand) {
            this.firstOperand = 0;
        } else {
            this.secondOperand = 0;
        }
        this._updateOperandValueText();
    }


}