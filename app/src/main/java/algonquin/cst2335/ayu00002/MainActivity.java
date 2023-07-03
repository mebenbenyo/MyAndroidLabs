package algonquin.cst2335.ayu00002;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides methods to check the complexity of a password.
 * @author Sewuese Ayu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This variable holds the text at the centre of the screen*/
    TextView textView = null;
    /** This variable stores the edit text where the password is typed*/
    EditText passwordText = null;
    /** This variable holds the Login button at the bottom of the screen*/
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        passwordText = findViewById(R.id.passwordText);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(click -> {
            String password = passwordText.getText().toString();

            checkPasswordComplexity(password);
        });
    }

    /**
     * This function checks if the password inputted is complex enough / meet the requirements specified
     * @param pw The String object that we are checking
     * @return Returns true if the password is complex enough, and false if it is not complex enough
     */
    boolean checkPasswordComplexity (String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;


        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {
            textView.setText("You shall not pass");
            Toast.makeText(getApplicationContext(), "Your password does not have an upper case letter", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!foundLowerCase) {
            textView.setText("You shall not pass");
            Toast.makeText(getApplicationContext(), "Your password does not have a lower case letter", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!foundNumber) {
            textView.setText("You shall not pass");
            Toast.makeText(getApplicationContext(), "Your password does not have a number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!foundSpecial) {
            textView.setText("You shall not pass");
            Toast.makeText(getApplicationContext(), "Your password does not have a special symbol", Toast.LENGTH_SHORT).show();
            return false;
        } else
            textView.setText("Your password meets the requirements.");
        return true;
    }

    /**
     * This function is called within the checkPasswordComplexity function.
     * It is used to check if the password inputted contains a special character.
     * @param c Each character within the iteration of the inputted password
     * @return true or false based on the results of the check
     */
    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '?':
            case'*':
            case'$':
            case '%':
            case'^':
            case'&':
            case'!':
            case'@':
                return true;
            default:
                return false;
        }
    }
}