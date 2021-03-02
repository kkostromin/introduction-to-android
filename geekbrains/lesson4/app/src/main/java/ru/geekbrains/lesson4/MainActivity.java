package ru.geekbrains.lesson4;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

/**
 * @author Zurbaevi Nika
 */
public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "nightModePreferences";
    public static final String KEY_NIGHT_MODE = "nightMode";
    private static final String KEY_MAIN_SCREEN = "mainScreen";
    private static final String KEY_MEMORY_SCREEN = "memoryScreen";
    private static final String KEY_EQUATION = "equation";
    SharedPreferences sharedPreferences;

    TextView mainScreen;
    TextView memoryScreen;
    SwitchCompat changeTheme;
    String equation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        changeTheme = findViewById(R.id.change_theme);
        mainScreen = findViewById(R.id.main_screen);
        memoryScreen = findViewById(R.id.memory_screen);

        checkNightModeActivated();

        changeTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveNightModeState(true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveNightModeState(false);
            }
            recreate();
        });
    }

    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NIGHT_MODE, nightMode).apply();
    }

    public void checkNightModeActivated() {
        if (sharedPreferences.getBoolean(KEY_NIGHT_MODE, false)) {
            changeTheme.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            changeTheme.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
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
        String[] inputs = equations[equations.length - 1].split(" ");
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
