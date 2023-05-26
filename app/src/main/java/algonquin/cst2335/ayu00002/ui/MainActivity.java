package algonquin.cst2335.ayu00002.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.ayu00002.data.MainViewModel;
import algonquin.cst2335.ayu00002.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        TextView textview = variableBinding.textview;
        Button myButton = variableBinding.myButton;
        EditText myEditText = variableBinding.myEditText;
        CheckBox checkBox = variableBinding.checkBox;
        RadioButton radioButton = variableBinding.radioButton;
        Switch mySwitch = variableBinding.mySwitch;
        ImageView myImage = variableBinding.myImage;
        ImageButton myImageButton = variableBinding.myImageButton;

        variableBinding.myButton.setOnClickListener(click ->
        {
            model.editString.postValue(variableBinding.myEditText.getText().toString());
        });

        model.editString.observe(this, s -> {
            variableBinding.textview.setText("Your edit text has: " + s);
        });




        myImageButton.setOnClickListener(click ->
        {
            CharSequence imgText = "The width = " + myImageButton.getWidth() + " and height = " + myImageButton.getHeight();
            Toast.makeText(getApplicationContext(), imgText, Toast.LENGTH_SHORT).show();
        });

        model.selected.observe(this, isSelected -> {
            checkBox.setChecked(isSelected);
            radioButton.setChecked(isSelected);
            mySwitch.setChecked(isSelected);

            CharSequence text = "The value is now: " + isSelected;
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        });

        checkBox.setOnCheckedChangeListener( (btn, isChecked) -> {
            model.selected.postValue(isChecked);
        } );
        radioButton.setOnCheckedChangeListener( (btn, isChecked) -> {
            model.selected.postValue(isChecked);
        } );
        mySwitch.setOnCheckedChangeListener( (btn, isChecked) -> {
            model.selected.postValue(isChecked);
        } );

    }
}