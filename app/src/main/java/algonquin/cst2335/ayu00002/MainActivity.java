package algonquin.cst2335.ayu00002;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import algonquin.cst2335.ayu00002.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    protected ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());


        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );
        setContentView(variableBinding.getRoot());


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        EditText previousEmail = variableBinding.emailText;
        previousEmail.setText(emailAddress);


        variableBinding.loginButton.setOnClickListener( (v) -> {
            Log.e(TAG, "You clicked the login button");

            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            String emailTyped = variableBinding.emailText.getText().toString();

            nextPage.putExtra("EmailAddress", emailTyped);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", emailTyped);
            editor.apply();
            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "MainActivity", "In onStart() - Application is visible on screen" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( "MainActivity", "In onResume() - Application is responding to user input" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( "MainActivity", "In onPause() - Application no longer responds to user input" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( "MainActivity", "In onStop() - Application no longer visible" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( "MainActivity", "In onDestroy() - Any memory used by application is freed" );
    }
}