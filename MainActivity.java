package com.example.khodadadi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    ImageView imageView;
    public String appid="App Key";
    TextView temptv, time, longitude, latitude, humidity, sunrise, sunset, pressure, wind, country, city_nam, max_temp, min_temp, feels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        temptv = findViewById(R.id.textView3);
        time = findViewById(R.id.textView2);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        humidity = findViewById(R.id.humidity);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        pressure = findViewById(R.id.pressure);
        wind = findViewById(R.id.wind);
        country = findViewById(R.id.country);
        city_nam = findViewById(R.id.city_nam);
        max_temp = findViewById(R.id.temp_max);
        min_temp = findViewById(R.id.min_temp);
        feels = findViewById(R.id.feels);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findWeather();
            }
        });
    }

    public void findWeather() {
        final String city = editText.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid="+appid+"&units=metric";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // Temperature
                            JSONObject mainObject = jsonObject.getJSONObject("main");
                            double temp = mainObject.getDouble("temp");
                            temptv.setText("Temperature\n" + temp + "°C");

                            // Country
                            JSONObject sysObject = jsonObject.getJSONObject("sys");
                            String countryName = sysObject.getString("country");
                            country.setText(countryName + ":");

                            // City
                            String cityName = jsonObject.getString("name");
                            city_nam.setText(cityName);

                            // Icon
                            JSONArray weatherArray = jsonObject.getJSONArray("weather");
                            JSONObject weatherObject = weatherArray.getJSONObject(0);
                            String icon = weatherObject.getString("icon");
                            Picasso.get().load("http://openweathermap.org/img/wn/" + icon + ".png").into(imageView);

                            // Date & Time
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat std = new SimpleDateFormat("HH:mm a \nE, MMM dd yyyy", Locale.getDefault());
                            String date = std.format(calendar.getTime());
                            time.setText(date);

                            // Latitude
                            JSONObject coordObject = jsonObject.getJSONObject("coord");
                            double latitudeValue = coordObject.getDouble("lat");
                            latitude.setText(latitudeValue + "° N");

                            // Longitude
                            double longitudeValue = coordObject.getDouble("lon");
                            longitude.setText(longitudeValue + "° E");

                            // Humidity
                            int humidityValue = mainObject.getInt("humidity");
                            humidity.setText(humidityValue + "%");

                            // Sunrise
                            long sunriseValue = sysObject.getLong("sunrise");
                            Calendar sunriseCalendar = Calendar.getInstance();
                            sunriseCalendar.setTimeInMillis(sunriseValue * 1000);
                            SimpleDateFormat sunriseFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                            sunrise.setText(sunriseFormat.format(sunriseCalendar.getTime()));

                            // Sunset
                            long sunsetValue = sysObject.getLong("sunset");
                            Calendar sunsetCalendar = Calendar.getInstance();
                            sunsetCalendar.setTimeInMillis(sunsetValue * 1000);
                            SimpleDateFormat sunsetFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                            sunset.setText(sunsetFormat.format(sunsetCalendar.getTime()));

                            // Pressure
                            String pressureValue = mainObject.getString("pressure");
                            pressure.setText(pressureValue + " hPa");

                            // Wind Speed
                            JSONObject windObject = jsonObject.getJSONObject("wind");
                            String windSpeed = windObject.getString("speed");
                            wind.setText(windSpeed+ " km/h");

                            // Min Temperature
                            double minTemp = mainObject.getDouble("temp_min");
                            min_temp.setText("Min Temp\n" + minTemp + " °C");

                            // Max Temperature
                            double maxTemp = mainObject.getDouble("temp_max");
                            max_temp.setText("Max Temp\n" + maxTemp + " °C");

                            // Feels Like
                            double feelsLike = mainObject.getDouble("feels_like");
                            feels.setText(feelsLike + " °C");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}