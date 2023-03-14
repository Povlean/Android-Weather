package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.JSON.Weather;
import com.example.weather.bean.Advice;
import com.example.weather.bean.AirQuality;
import com.example.weather.bean.Daily;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";

    public DrawerLayout drawerLayout;

    private ScrollView weatherLayout;

    private Button navButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    private String mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // 初始化各控件
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);

        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);

        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);

        TextView dateText = (TextView) view.findViewById(R.id.date_text);
        TextView infoText = (TextView) view.findViewById(R.id.info_text);
        TextView maxText = (TextView) view.findViewById(R.id.max_text);
        TextView minText = (TextView) view.findViewById(R.id.min_text);

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String locationID = intent.getStringExtra("LocationID");
        String countyName = intent.getStringExtra("CountyName");

        requestWeather(locationID,countyName);
    }

    public Advice advice;
    public void showAdvice(String locationID) {
        String url = "https://devapi.qweather.com/v7/indices/1d?type=1,2,3&location="+locationID+"&key=d21c461963234236bf12913f0b161aa5";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();

                    Response response = client.newCall(request).execute();
                    String strJson = response.body().string();

                    JSONObject jsonObject = new JSONObject(strJson);
                    JSONArray daily = jsonObject.getJSONArray("daily");

                    String comfort = null,wash = null,sport = null;
                    for(int i = 0;i < daily.length();i++){
                        JSONObject jsonObj = daily.getJSONObject(i);
                        if(i == 0) {
                            comfort = jsonObj.getString("text");
                        }
                        if(i == 1) {
                            wash = jsonObj.getString("text");
                        }
                        if(i == 2) {
                            sport = jsonObj.getString("text");
                        }
                    }
                    advice = new Advice(comfort,wash,sport);
                    Log.d(TAG, "advice: " + advice);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public AirQuality airQuality;
    public void showAir(String locationID) {
        String url = "https://devapi.qweather.com/v7/air/now?location="+locationID+"&key=d21c461963234236bf12913f0b161aa5";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();

                    Response response = client.newCall(request).execute();
                    String strJson = response.body().string();

                    JSONObject jsonObject = new JSONObject(strJson);
                    String now = jsonObject.getString("now");

                    JSONObject jsonObject1 = new JSONObject(now);
                    String aqi = jsonObject1.getString("aqi");
                    String pm2p5 = jsonObject1.getString("pm2p5");

                    airQuality = new AirQuality(aqi,pm2p5);
                    Log.d(TAG, "run: " + airQuality);
                }catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            aqiText = (TextView) findViewById(R.id.aqi_text);
                            pm25Text = (TextView) findViewById(R.id.pm25_text);
                        }
                    });
                }
            }
        }).start();
    }

    public List<Daily> dailyList = new ArrayList<>();
    public void forecastData(String locationID) {
        String url = "https://devapi.qweather.com/v7/weather/3d?location="+locationID+"&key=d21c461963234236bf12913f0b161aa5";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();

                    Response response = client.newCall(request).execute();
                    String strJson = response.body().string();

                    JSONObject jsonObject = new JSONObject(strJson);
                    String daily = jsonObject.getString("daily");

                    JSONArray jsonArray = new JSONArray(daily);
                    Log.d(TAG, "daily: " + jsonArray);
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonDay = jsonArray.getJSONObject(i);
                        String fxDate = jsonDay.getString("fxDate");
                        String tempMax = jsonDay.getString("tempMax");
                        String tempMin = jsonDay.getString("tempMin");
                        String textDay = jsonDay.getString("textDay");
                        Daily daily1 = new Daily(fxDate,tempMax,tempMin,textDay);
                        Log.d(TAG, "run: " + daily1);
                        dailyList.add(daily1);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public String tempText,textText,updateTime;
    public void dataCallBack(String locationID) {
        String url = "https://devapi.qweather.com/v7/weather/now?location="+locationID+"&key=d21c461963234236bf12913f0b161aa5";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();

                    Response response = client.newCall(request).execute();
                    String strJson = response.body().string();

                    JSONObject jsonObject = new JSONObject(strJson);
                    String now = jsonObject.getString("now");
                    updateTime = jsonObject.getString("updateTime");

                    JSONObject jsonObj = new JSONObject(now);
                    String temp = jsonObj.getString("temp");
                    String text = jsonObj.getString("text");
                    if(temp != null && text != null) {
                        tempText = temp;
                        textText = text;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    public void requestWeather(String locationID,String countyName) {

        dataCallBack(locationID);

        forecastData(locationID);

        showAir(locationID);

        showAdvice(locationID);

        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        degreeText.setText(tempText + "℃");
        weatherInfoText.setText(textText);
        titleCity.setText(countyName);
        forecastLayout.removeAllViews();
        for(Daily daily : dailyList) {
            View view01 = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText01 = (TextView) view01.findViewById(R.id.date_text);
            TextView infoText01 = (TextView) view01.findViewById(R.id.info_text);
            TextView maxText01 = (TextView) view01.findViewById(R.id.max_text);
            TextView minText01 = (TextView) view01.findViewById(R.id.min_text);
            dateText01.setText(daily.getFxDate());
            infoText01.setText(daily.getTextDay());
            maxText01.setText(daily.getTempMax());
            minText01.setText(daily.getTempMin());
            forecastLayout.addView(view01);
        }

        try {

            aqiText.setText(airQuality.getAqi());
            pm25Text.setText(airQuality.getPm25());

            comfortText.setText(advice.getClothAdv());
            carWashText.setText(advice.getWashAdv());
            sportText.setText(advice.getSportAdv());

        } catch (Exception e) {
            Log.d(TAG, "requestWeather: " + airQuality);
            e.printStackTrace();
        }
    }

}
