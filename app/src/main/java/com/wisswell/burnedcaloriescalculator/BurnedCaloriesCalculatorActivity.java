package com.wisswell.burnedcaloriescalculator;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.inputmethod.EditorInfo;

import java.text.NumberFormat;


public class BurnedCaloriesCalculatorActivity extends Activity
        implements OnKeyListener, OnEditorActionListener, OnItemSelectedListener, OnSeekBarChangeListener {

    // define variables for the widgets
    private EditText weightTextView;
    private TextView milesRanTextView;
    private SeekBar milesRanSeekBar;
    private TextView caloriesBurnedTextView;
    private Spinner heightFeetSpinner;
    private Spinner heightInchesSpinner;
    private TextView bmiTextView;
    private EditText nameEditText;

    // define the SharedPreferences object
    private SharedPreferences savedValues;

    // define instance variables
    private String weightString = "";
    private int split = 1;
    float milesRan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        // get references to the widgets
        weightTextView = (EditText) findViewById(R.id.weightTextView);
        milesRanTextView = (TextView) findViewById(R.id.milesRanTextView);
        milesRanSeekBar = (SeekBar) findViewById(R.id.milesRanSeekBar);
        caloriesBurnedTextView = (TextView) findViewById(R.id.caloriesBurnedTextView);
        heightFeetSpinner = (Spinner) findViewById(R.id.heightFeetSpinner);
        heightInchesSpinner = (Spinner) findViewById(R.id.heightInchesSpinner);
        bmiTextView = (TextView) findViewById(R.id.bmiTextView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);


        // set array adapter for spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.height_feet_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        heightFeetSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.height_inches_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        heightInchesSpinner.setAdapter(adapter1);

        // set the listeners
        weightTextView.setOnEditorActionListener(this);
        weightTextView.setOnKeyListener(this);
        milesRanSeekBar.setOnSeekBarChangeListener(this);
        milesRanSeekBar.setOnKeyListener(this);
        heightFeetSpinner.setOnItemSelectedListener(this);
        heightInchesSpinner.setOnItemSelectedListener(this);


        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }

    @Override
    protected void onPause() {

        // save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("weightString", weightString);
        editor.putFloat("milesRan", milesRan);
        editor.putInt("split", split);
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // get the instance variables
        weightString = savedValues.getString("weightString", "");
        milesRan = savedValues.getFloat("milesRan", 1);
        split = savedValues.getInt("split", 1);

        // set the weight amount on its widget
        weightTextView.setText(weightString);

        // set the miles ran on its widget
        int progress = (int) milesRan;
        milesRanSeekBar.setProgress(progress);


    }

    public void calculateAndDisplay(){

        //get weight
        weightString = weightTextView.getText().toString();
        float weight;

        if (weightString.equals("")) {
            weight = 0;
        }
        else {
            weight = Float.parseFloat(weightString);
        }

        //get milesRan
        milesRan = milesRanSeekBar.getProgress();

        //calculate calories burned
        double caloriesBurned = 0.75 * weight * milesRan;

        //get feet
        String feetString = (String) heightFeetSpinner.getSelectedItem();
        float feet = Float.parseFloat(feetString);

        //get inches
        String inchesString = (String) heightInchesSpinner.getSelectedItem();
        float inches = Float.parseFloat(inchesString);

        //calculate BMI
        double bmi = (weight * 703) / ((12 * feet + inches) * (12 * feet + inches));

        // display the results with formatting
        NumberFormat integer = NumberFormat.getIntegerInstance();
        caloriesBurnedTextView.setText(integer.format(caloriesBurned));
        bmiTextView.setText(integer.format(bmi));


    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE ||
                i == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }

        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        split = position + 1;
        calculateAndDisplay();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int milesRan, boolean b) {
       milesRanTextView.setText(milesRan);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        calculateAndDisplay();
    }
}
