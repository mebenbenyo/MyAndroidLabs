package algonquin.cst2335.ayu00002;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.ayu00002.databinding.ActivityMainBinding;

/**
 * This class provides methods to check the complexity of a password.
 * @author Sewuese Ayu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    protected  String cityName;
    RequestQueue queue = null;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);

    binding.getForecast.setOnClickListener(click -> {
        cityName = binding.cityText.getText().toString();
        String stringURL = null;
        try {
             stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                    + URLEncoder.encode(cityName, "UTF-8")
                    + "&appid=ad2928b06e1d92be33e145eebf08a906&units=metric";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                ( response ) -> {
                    try {
                        JSONObject coord = response.getJSONObject("coord");
                        JSONArray weatherArray = response.getJSONArray ( "weather" );
                        JSONObject position0 = weatherArray.getJSONObject(0);

                        int vis = response.getInt("visibility");
                        String name = response.getString("name");

                        JSONObject mainObject = response.getJSONObject("main");
                        double current = mainObject.getDouble("temp");
                        double min = mainObject.getDouble("temp_min");
                        double max = mainObject.getDouble("temp_max");
                        int humidity = mainObject.getInt("humidity");
                        String description = position0.getString("description");
                        String iconName = position0.getString("icon");

                        String imageURL = "https://openweathermap.org/img/w/" + iconName + ".png";

                            ImageRequest imgReq = new ImageRequest(imageURL, bitmap -> {
                                FileOutputStream fOut = null;
                                try {
                                    fOut = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                    image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                    fOut.flush();
                                    fOut.close();
                                    binding.icon.setImageBitmap(image);

                                    runOnUiThread(() -> {
                                        binding.temp.setText("The current temperature is " + current + " degrees");
                                        binding.temp.setVisibility(View.VISIBLE);

                                        binding.minTemp.setText("The min temperature is " + min + " degrees");
                                        binding.minTemp.setVisibility(View.VISIBLE);

                                        binding.maxTemp.setText("The max temperature is " + max + " degrees");
                                        binding.maxTemp.setVisibility(View.VISIBLE);

                                        binding.humidity.setText("The humidity is " + humidity + "%");
                                        binding.humidity.setVisibility(View.VISIBLE);

                                        binding.icon.setImageBitmap(image);
                                        binding.icon.setVisibility(View.VISIBLE);

                                        binding.description.setText(description);
                                        binding.description.setVisibility(View.VISIBLE);
                                    });
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                            });
                            queue.add(imgReq);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                ( error) -> {        });
        queue.add(request);
    });
    }


}