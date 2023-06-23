package algonquin.cst2335.ayu00002;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.ayu00002.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    protected ActivitySecondBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        variableBinding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phoneNumberObj = prefs.getString("phoneNumberObj", "");
        EditText phone = variableBinding.editTextPhone;
        phone.setText(phoneNumberObj);



        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        variableBinding.textView.setText("Welcome back " + emailAddress);

        variableBinding.callButton.setOnClickListener((v) -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            String phoneNumber = variableBinding.editTextPhone.getText().toString();
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);
        });

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File( getFilesDir(), "Picture.png");
        if(file.exists())
        {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            ImageView profileImage = variableBinding.imageView;
            profileImage.setImageBitmap(theImage);
        }

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");

                            FileOutputStream fOut = null;

                            try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }
                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            catch(IOException exception) {
                                exception.printStackTrace();
                                Log.w("SecondActivity","IOException caught");
                            }

                            variableBinding.imageView.setImageBitmap( thumbnail );
                        }
                        else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                            Log.w("SecondActivity", "Result is cancelled");
                        }
                    }
                });

        variableBinding.pictureButton.setOnClickListener((v) -> {

            cameraResult.launch(cameraIntent);
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( "MainActivity", "In onPause() - Application no longer responds to user input" );

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        prefs.getString("phoneNumber", "");

        EditText phoneNumberObj = variableBinding.editTextPhone;


        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumberObj", phoneNumberObj.getText().toString());
        editor.apply();

    }
}