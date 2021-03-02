package ru.geekbrains.lesson3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_MAIN_SCREEN = "mainScreen";
    private static final String KEY_MEMORY_SCREEN = "memoryScreen";
    private static final String KEY_EQUATION = "equation";

    TextView mainScreen;
    TextView memoryScreen;
    String equation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainScreen = findViewById(R.id.main_screen);
        memoryScreen = findViewById(R.id.memory_screen);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_MAIN_SCREEN, mainScreen.getText().toString());
        outState.putString(KEY_MEMORY_SCREEN, memoryScreen.getText().toString());
        outState.putString(KEY_EQUATION, equation);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mainScreen.setText(savedInstanceState.getString(KEY_MAIN_SCREEN));
        memoryScreen.setText(savedInstanceState.getString(KEY_MEMORY_SCREEN));
        equation = savedInstanceState.getString(KEY_EQUATION);
    }

    public void press(View view) {
        String input = mainScreen.getText().toString();
        Button button = (Button) view;
        String key = button.getText().toString();
        switch (key) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "0": {
                String last = !equation.isEmpty() ? equation.substring(equation.length() - 3) : "";
                if (last.contains("=")) {
                    Toast.makeText(this, "Must enter an operator first", Toast.LENGTH_LONG).show();
                    break;
                }
                mainScreen.setText(String.format("%s%s", input, key));
                break;
            }
            case "+":
            case "-":
            case "/":
            case "x": {
                if (!equation.isEmpty() || !input.isEmpty()) {
                    addToEquation(key);
                }
                break;
            }
            case "=": {
                if (!equation.isEmpty() || !input.isEmpty()) {
                    addToEquation(key);
                    mainScreen.setText(calculate());
                }
            }
        }
    }

    public void addToEquation(String key) {
        String input = mainScreen.getText().toString();
        String operation = String.format(" %s ", key);
        if (equation.length() > 3 && equation.endsWith(" ") && input.equals("")) {
            equation = equation.substring(0, equation.length() - 3);
        }
        equation = String.format("%s%s%s", equation, input, operation);
        memoryScreen.setText(equation);
        mainScreen.setText("");
    }

    private boolean matches(String regex) {
        for (int i = 0; i < regex.length(); i++) {
            if (regex.charAt(i) != '+' && regex.charAt(i) != '-' && regex.charAt(i) != '/' && regex.charAt(i) != 'x') {
                return false;
            }
        }
        return true;
    }

    public String calculate() {
        String[] equations = equation.split(" = ");
        String currentEquation = equations[equations.length - 1];
        String[] inputs = currentEquation.split(" ");
        double total = 0;
        String operator = "+";
        for (String value : inputs) {
            if (matches(value)) {
                operator = value;
            } else if (!value.equals("=")) {
                switch (operator) {
                    case "+":
                        total += Double.parseDouble(value);
                        break;
                    case "-":
                        total -= Double.parseDouble(value);
                        break;
                    case "x":
                        total *= Double.parseDouble(value);
                        break;
                    case "/":
                        total /= Double.parseDouble(value);
                        break;
                }
            }
        }
        return String.valueOf(total);
    }

    public void clear(View view) {
        memoryScreen.setText("");
        mainScreen.setText("");
        equation = "";
    }
}
