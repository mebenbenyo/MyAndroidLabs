package algonquin.cst2335.ayu00002;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.ayu00002.databinding.ActivityMainBinding;


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
                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject position0 = weatherArray.getJSONObject(0);
//                        JSONObject coord = response.getJSONObject("coord");
//                        int vis = response.getInt("visibility");
//                        String name = response.getString("name");
                        String iconName = position0.getString("icon");
                        String description = position0.getString("description");

                        JSONObject mainObject = response.getJSONObject("main");
                        double current = mainObject.getDouble("temp");
                        double min = mainObject.getDouble("temp_min");
                        double max = mainObject.getDouble("temp_max");
                        int humidity = mainObject.getInt("humidity");

                        runOnUiThread(() -> {

                            binding.temp.setText("The current temperature is " + current + " degrees");
                            binding.temp.setVisibility(View.VISIBLE);

                            binding.minTemp.setText("The min temperature is " + min + " degrees");
                            binding.minTemp.setVisibility(View.VISIBLE);

                            binding.maxTemp.setText("The max temperature is " + max + " degrees");
                            binding.maxTemp.setVisibility(View.VISIBLE);

                            binding.humidity.setText("The humidity is " + humidity + "%");
                            binding.humidity.setVisibility(View.VISIBLE);

                            binding.description.setText(description);
                            binding.description.setVisibility(View.VISIBLE);
                        });

                        String imageURL = getFilesDir() + "/" + iconName + ".png";
                        File file = new File(imageURL);

                        if (file.exists()) {
                            image = BitmapFactory.decodeFile(imageURL);
                            runOnUiThread(() -> {
                                binding.icon.setImageBitmap(image);
                                binding.icon.setVisibility(View.VISIBLE);
                            });
                        }  else {
                            String imagePath = "http://openweathermap.org/img/w/" + iconName + ".png";
                            ImageRequest imgReq = new ImageRequest(imagePath, bitmap -> {
                                        try {
                                            image = bitmap;
                                            image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName + ".png", Context.MODE_PRIVATE));
                                            runOnUiThread(() -> {
                                                binding.icon.setImageBitmap(image);
                                                binding.icon.setVisibility(View.VISIBLE);
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                    error -> Log.e("ImageRequestError", error.toString()));
                            queue.add(imgReq);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> Log.e("JsonObjectRequestError", error.toString()));

        queue.add(request);
    });
    }
}