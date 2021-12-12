package vn.hust.edu.navigationdrawerapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import vn.hust.edu.navigationdrawerapp.R;


public class NumberConverterFragment extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher {


    private String fromUnit;
    private String toUnit;
    private String valToConvert = null;
    private String result = null;
    private View rootView;

    private static final String BINARY_UNIT = "binary";
    private static final String OCTAL_UNIT = "octal";
    private static final String DECIMAL_UNIT = "decimal";
    private static final String HEX_UNIT = "hexadecimal";


    public NumberConverterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_number_converter, container, false);



        TextView inputTextView = (TextView) rootView.findViewById(R.id.fromInput);
        inputTextView.addTextChangedListener(this);

        Spinner fromUnitSpinner = (Spinner) rootView.findViewById(R.id.fromUnitSpinner);
        Spinner toUnitSpinner = (Spinner) rootView.findViewById(R.id.toUnitSpinner);

        ArrayAdapter<CharSequence> fromAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.units, android.R.layout.simple_spinner_item);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<CharSequence> toAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.units, android.R.layout.simple_spinner_item);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromUnitSpinner.setAdapter(fromAdapter);
        // Set default from unit to decimal
        fromUnitSpinner.setSelection(2);

        toUnitSpinner.setAdapter(toAdapter);
        // Set default unit to binary
        toUnitSpinner.setSelection(0);

        fromUnitSpinner.setOnItemSelectedListener(this);
        toUnitSpinner.setOnItemSelectedListener(this);

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        int spinnerId = adapterView.getId();
        Log.i("selected", String.valueOf(pos));
        if (spinnerId == R.id.fromUnitSpinner) {
            Log.i("spinner", "from");
            switch (pos) {
                case 0:
                    this.fromUnit = BINARY_UNIT;
                    break;

                case 1:
                    this.fromUnit = OCTAL_UNIT;
                    break;

                case 2:
                    this.fromUnit = DECIMAL_UNIT;
                    break;

                case 3:
                    this.fromUnit = HEX_UNIT;
            }
        } else {
            Log.i("spinner", "to");
            switch (pos) {
                case 0:
                    this.toUnit = BINARY_UNIT;
                    break;

                case 1:
                    this.toUnit = OCTAL_UNIT;
                    break;

                case 2:
                    this.toUnit = DECIMAL_UNIT;
                    break;

                case 3:
                    this.toUnit = HEX_UNIT;
            }
        }
        if (this.valToConvert != null) {
            this.result = this.convert();
            this.displayResult();
        }
        if (this.fromUnit != null) {
            Log.i("from_unit", this.fromUnit);
        }
        if (this.toUnit != null) {
            Log.i("to_unit", this.toUnit);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0) {
            this.valToConvert = charSequence.toString();
        } else {
            this.valToConvert = null;
        }
        this.result = this.convert();
        this.displayResult();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void displayResult() {
        TextView resultView = (TextView) rootView.findViewById(R.id.result);
        resultView.setText(this.result);
    }

    private String convert() {
        if (this.valToConvert == null || this.fromUnit == null || this.toUnit == null) {
            return null;
        }

        switch (this.fromUnit) {
            case BINARY_UNIT:
                switch (this.toUnit) {
                    case BINARY_UNIT:
                        return this.valToConvert;

                    case OCTAL_UNIT:
                        return this.binaryToOctal(this.valToConvert);

                    case DECIMAL_UNIT:
                        return this.binaryToDecimal(this.valToConvert);

                    case HEX_UNIT:
                        return this.binaryToHex(this.valToConvert);
                }

            case OCTAL_UNIT:
                switch (this.toUnit) {
                    case BINARY_UNIT:
                        return this.octalToBinary(this.valToConvert);

                    case OCTAL_UNIT:
                        return this.valToConvert;

                    case DECIMAL_UNIT:
                        return this.octalToDecimal(this.valToConvert);

                    case HEX_UNIT:
                        return this.octalToHex(this.valToConvert);
                }

            case DECIMAL_UNIT:
                switch (this.toUnit) {
                    case BINARY_UNIT:
                        return this.decimalToBinary(this.valToConvert);

                    case OCTAL_UNIT:
                        return this.decimalToOctal(this.valToConvert);

                    case DECIMAL_UNIT:
                        return this.valToConvert;

                    case HEX_UNIT:
                        return this.decimalToHex(this.valToConvert);
                }

            case HEX_UNIT:
                switch (this.toUnit) {
                    case BINARY_UNIT:
                        return this.hexToBinary(this.valToConvert);

                    case OCTAL_UNIT:
                        return this.hexToOctal(this.valToConvert);

                    case DECIMAL_UNIT:
                        return this.hexToDecimal(this.valToConvert);

                    case HEX_UNIT:
                        return this.valToConvert;
                }

            default:
                return null;
        }
    }

    private String decimalToBinary(String val) {
        if (!val.matches("^[0-9]+$")) {
            return null;
        };

        String reverseRes = "";

        double decimal = Double.parseDouble(val);

        if (decimal == 0) {
            return String.valueOf(0);
        }

        while (decimal > 0) {
            reverseRes += String.valueOf(((int) (decimal % 2)));
            decimal = (int) (decimal / 2);
        }

        String res = "";
        for (int i = reverseRes.length() - 1; i >= 0; i--) {
            res += reverseRes.charAt(i);
        }

        return res;
    }

    private String decimalToOctal(String val) {
        String binVal = this.decimalToBinary(val);
        return this.binaryToOctal(binVal);
    }

    private String decimalToHex(String val) {
        String binVal = this.decimalToBinary(val);
        return this.binaryToHex(binVal);
    }

    private String binaryToOctal(String val) {
        if (val == null || !this.isBinary(val)) {
            return null;
        }

        int len = val.length();
        String res = "";
        String reverseRes = "";
        for (int i = len - 1; i >= 0; i -= 3) {
            int digitRes = 0;
            for (int j = i; j >= Math.max(i - 2, 0); j--) {
                if (val.charAt(j) == '1') {
                    digitRes += Math.pow(2, i - j);
                }
            }
            reverseRes += String.valueOf(digitRes);
        }
        for (int i = reverseRes.length() - 1; i >= 0; i--) {
            res += reverseRes.charAt(i);
        }
        return res;
    }

    private String binaryToDecimal(String val) {
        if (val == null || !this.isBinary(val)) {
            return null;
        }

        double res = 0;
        int len = val.length();
        for (int i = len - 1; i >= 0; i--) {
            if (val.charAt(i) == '1') {
                res += Math.pow(2, len - 1 - i);
            }
        }
        return String.valueOf(((int) res));
    }

    private  String binaryToHex(String val) {
        if (val == null || !this.isBinary(val)) {
            return null;
        }

        int len = val.length();

        String res = "";
        String reverseRes = "";
        for (int i = len - 1; i >= 0; i -= 4) {
            int digitRes = 0;
            for (int j = i; j >= Math.max(i - 3, 0); j--) {
                if (val.charAt(j) == '1') {
                    digitRes += Math.pow(2, i - j);
                }
            }
            switch (digitRes) {
                case 10:
                    reverseRes += 'A';
                    break;

                case 11:
                    reverseRes += 'B';
                    break;

                case 12:
                    reverseRes += 'C';
                    break;

                case 13:
                    reverseRes += 'D';
                    break;

                case 14:
                    reverseRes += 'E';
                    break;

                case 15:
                    reverseRes += 'F';
                    break;

                default:
                    reverseRes += String.valueOf(digitRes);
            }
        }
        for (int i = reverseRes.length() - 1; i >= 0; i--) {
            res += reverseRes.charAt(i);
        }

        return res;
    }

    private String octalToBinary(String val) {
        if (val == null || !this.isOctal(val)) {
            return null;
        }


        int len = val.length();
        String res = "";
        for (int i = 0; i < len; i++) {
            if (i == 0 && val.charAt(i) == '0') {
                continue;
            }
            if (val.charAt(i) == '0') {
                res += "000";
            }
            String curDigit = "" + val.charAt(i);
            String binVal = this.decimalToBinary(curDigit);
            int binLen = binVal.length();
            if (binLen < 3 && i > 0) {
                for (int j = 0; j < 3 - binLen; ++j) {
                    res += '0';
                }
            }
            res += binVal;
        }
        return res;
    }

    private String octalToDecimal(String val) {
        if (val == null || !this.isOctal(val)) {
            return null;
        }

        if (val.length() == 1) {
            return val;
        }

        String binVal = this.octalToBinary(val);
        return this.binaryToDecimal(binVal);
    }

    private  String octalToHex(String val) {
        if (val == null || !this.isOctal(val)) {
            return null;
        }

        String binVal = this.octalToBinary(val);
        return this.binaryToHex(binVal);
    }

    private String hexToBinary(String val) {
        if (val == null || !this.isHexa(val)) {
            return null;
        }

        int len = val.length();
        String res = "";
        for (int i = 0; i < len; i++) {
            if (i == 0 && val.charAt(i) == '0') {
                continue;
            }

            String curDigit = "" + val.charAt(i);
            switch (curDigit.toUpperCase()) {
                case "A":
                    res += "1010";
                    break;

                case "B":
                    res += "1011";
                    break;

                case "C":
                    res += "1100";
                    break;

                case "D":
                    res += "1101";
                    break;

                case "E":
                    res += "1110";
                    break;

                case "F":
                    res += "1111";
                    break;

                case "0":
                    res += "0000";
                    break;

                default:
                    String binVal = this.decimalToBinary(curDigit);
                    int binLen = binVal.length();
                    if (binLen < 4 && i > 0) {
                        for (int j = 0; j < 3 - binLen; ++j) {
                            res += '0';
                        }
                    }
                    res += binVal;
            }

        }
        return res;
    }

    private String hexToOctal(String val) {
        if (val == null || !this.isHexa(val)) {
            return null;
        }

        String binVal = this.hexToBinary(val);
        return this.binaryToOctal(binVal);
    }

    private  String hexToDecimal(String val) {
        if (val == null || !this.isHexa(val)) {
            return null;
        }

        String binVal = this.hexToBinary(val);
        return this.binaryToDecimal(binVal);
    }

    private boolean isBinary(String val) {
        return val.matches("^[0-1]+$");
    }

    private boolean isOctal(String val) {
        return val.matches("^[0-7]+$");
    }

    private boolean isDecimal(String val) {
        return val.matches("^[0-9]$");
    }

    private boolean isHexa(String val) {
        return val.matches("^[0-9a-fA-F]+$");
    }
}