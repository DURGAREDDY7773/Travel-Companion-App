package com.example.travelcompanionapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    EditText editTextValue;
    Button buttonConvert;
    TextView textViewResult;

    String[] categories = {"Currency", "Fuel", "Temperature"};
    String[] currencyUnits = {"USD", "AUD", "EUR", "JPY", "GBP"};
    String[] fuelUnits = {"mpg", "km/L", "Gallon", "Liter", "Nautical Mile", "Kilometer"};
    String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        editTextValue = findViewById(R.id.editTextValue);
        buttonConvert = findViewById(R.id.buttonConvert);
        textViewResult = findViewById(R.id.textViewResult);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        updateUnits("Currency");

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnits(categories[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        buttonConvert.setOnClickListener(v -> convert());
    }

    private void updateUnits(String category) {
        String[] units;

        if (category.equals("Currency")) {
            units = currencyUnits;
        } else if (category.equals("Fuel")) {
            units = fuelUnits;
        } else {
            units = temperatureUnits;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
    }

    private void convert() {
        String text = editTextValue.getText().toString();

        if (text.isEmpty()) {
            Toast.makeText(this, "Enter value", Toast.LENGTH_SHORT).show();
            return;
        }

        double value;
        try {
            value = Double.parseDouble(text);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spinnerCategory.getSelectedItem().toString();
        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();

        if (from.equals(to)) {
            textViewResult.setText("Result: " + value);
            return;
        }

        if (category.equals("Fuel") && value < 0) {
            Toast.makeText(this, "Negative not allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        double result = 0;

        if (category.equals("Currency")) {
            result = currencyConvert(from, to, value);
        } else if (category.equals("Fuel")) {
            result = fuelConvert(from, to, value);
        } else {
            result = tempConvert(from, to, value);
        }

        textViewResult.setText("Result: " + result);
    }

    private double currencyConvert(String from, String to, double value) {
        double usd = value;

        if (from.equals("AUD")) usd = value / 1.55;
        if (from.equals("EUR")) usd = value / 0.92;
        if (from.equals("JPY")) usd = value / 148.5;
        if (from.equals("GBP")) usd = value / 0.78;

        if (to.equals("AUD")) return usd * 1.55;
        if (to.equals("EUR")) return usd * 0.92;
        if (to.equals("JPY")) return usd * 148.5;
        if (to.equals("GBP")) return usd * 0.78;

        return usd;
    }

    private double fuelConvert(String from, String to, double value) {
        if (from.equals("mpg") && to.equals("km/L")) return value * 0.425;
        if (from.equals("km/L") && to.equals("mpg")) return value / 0.425;
        if (from.equals("Gallon") && to.equals("Liter")) return value * 3.785;
        if (from.equals("Liter") && to.equals("Gallon")) return value / 3.785;
        if (from.equals("Nautical Mile") && to.equals("Kilometer")) return value * 1.852;
        if (from.equals("Kilometer") && to.equals("Nautical Mile")) return value / 1.852;
        return value;
    }

    private double tempConvert(String from, String to, double value) {
        if (from.equals("Celsius") && to.equals("Fahrenheit")) return value * 1.8 + 32;
        if (from.equals("Fahrenheit") && to.equals("Celsius")) return (value - 32) / 1.8;
        if (from.equals("Celsius") && to.equals("Kelvin")) return value + 273.15;
        if (from.equals("Kelvin") && to.equals("Celsius")) return value - 273.15;
        if (from.equals("Fahrenheit") && to.equals("Kelvin")) return (value - 32) / 1.8 + 273.15;
        if (from.equals("Kelvin") && to.equals("Fahrenheit")) return (value - 273.15) * 1.8 + 32;
        return value;
    }
}