package ru.geekbrains.lesson5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * @author Zurbaevi Nika
 */
public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private TextView mainScreen;
    private TextView memoryScreen;
    private Button settings;

    private Calculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);

        settings = findViewById(R.id.settings);
        mainScreen = findViewById(R.id.main_screen);
        memoryScreen = findViewById(R.id.memory_screen);

        calculator = new Calculator(mainScreen, memoryScreen);

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, RESULT_OK);
        });

        checkNightModeActivated();
    }


    public void checkNightModeActivated() {
        if (sharedPreferences.getBoolean(Constants.KEY_NIGHT_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.KEY_MAIN_SCREEN, mainScreen.getText().toString());
        outState.putString(Constants.KEY_MEMORY_SCREEN, memoryScreen.getText().toString());
        outState.putString(Constants.KEY_EQUATION, calculator.getEquation());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mainScreen.setText(savedInstanceState.getString(Constants.KEY_MAIN_SCREEN));
        memoryScreen.setText(savedInstanceState.getString(Constants.KEY_MEMORY_SCREEN));
        calculator.setEquation(savedInstanceState.getString(Constants.KEY_EQUATION));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RESULT_CANCELED) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (resultCode == RESULT_OK) {
            saveNightModeState(data.getExtras().getBoolean(Constants.KEY_NIGHT_MODE));
            recreate();
        }
    }

    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.KEY_NIGHT_MODE, nightMode).apply();
    }

    public void press(View view) {
        String input = mainScreen.getText().toString();
        Button button = (Button) view;
        switch (button.getId()) {
            case R.id.button_1:
            case R.id.button_2:
            case R.id.button_3:
            case R.id.button_4:
            case R.id.button_5:
            case R.id.button_6:
            case R.id.button_7:
            case R.id.button_8:
            case R.id.button_9:
            case R.id.button_0: {
                String last = !calculator.getEquation().isEmpty() ? calculator.getEquation().substring(calculator.getEquation().length() - 3) : "";
                if (last.contains("=")) {
                    Toast.makeText(this, "Must enter an operator first", Toast.LENGTH_LONG).show();
                    break;
                }
                mainScreen.setText(String.format("%s%s", input, button.getText().toString()));
                break;
            }
            case R.id.button_plus:
            case R.id.button_minus:
            case R.id.button_divide:
            case R.id.button_multiply: {
                if (!calculator.getEquation().isEmpty() || !input.isEmpty()) {
                    calculator.addToEquation(button.getText().toString());
                }
                break;
            }
            case R.id.button_equal: {
                if (!calculator.getEquation().isEmpty() || !input.isEmpty()) {
                    calculator.addToEquation(button.getText().toString());
                    mainScreen.setText(calculator.calculate());
                }
            }
        }
    }

    public void clear(View view) {
        memoryScreen.setText("");
        mainScreen.setText("");
        calculator.setEquation("");
    }
}
