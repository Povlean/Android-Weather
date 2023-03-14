package com.example.weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.weather.db.City;
import com.example.weather.db.County;
import com.example.weather.db.Province;
import com.example.weather.util.Constant;
import com.example.weather.util.HttpUtil;
import com.example.weather.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {
    private static final String TAG = "ChooseAreaFragment";

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList = new ArrayList<>(); //省列表
    private List<City> cityList = new ArrayList<>(); //市列表
    private List<County> countyList = new ArrayList<>();//县列表

    private Province selectedProvince;//选中的省份
    private City selectedCity;//选中的城市
    private County selectedCounty;//选中的第三级城市
    private int currentLevel; //当前选中的级别

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requireActivity().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event.getTargetState() == Lifecycle.State.CREATED) {
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (currentLevel == LEVEL_PROVINCE) {
                                // 交互
                                selectedProvince = provinceList.get(i);
                                queryCities();
                            } else if (currentLevel == LEVEL_CITY) {
                                selectedCity = cityList.get(i);
                                queryCounties();
                            } else if (currentLevel == LEVEL_COUNTY) {
                                selectedCounty = countyList.get(i);
                                getLocationID(selectedCounty.getCountyName());
                                if(getActivity() instanceof  MainActivity) {
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(),WeatherActivity.class);
                                    intent.putExtra("LocationID",locationID);
                                    intent.putExtra("CountyName",selectedCounty.getCountyName());
                                    startActivity(intent);
                                } else if(getActivity() instanceof WeatherActivity) {
                                    WeatherActivity activity = (WeatherActivity) getActivity();
                                    activity.drawerLayout.closeDrawers();
                                    activity.requestWeather(locationID,selectedCounty.getCountyName());
                                }
                            }
                        }
                    });

                    backButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentLevel == LEVEL_COUNTY) {
                                queryCities();
                            } else if (currentLevel == LEVEL_CITY) {
                                queryProvinces();
                            }
                        }
                    });
                    queryProvinces();
                    getLifecycle().removeObserver(this);
                }
            }
        });
    }

    private String locationID;
    public void getLocationID(String countyName) {
        String url = "https://geoapi.qweather.com/v2/city/lookup?location="+ countyName +"&key=d21c461963234236bf12913f0b161aa5";
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
                    String location = jsonObject.getString("location");
                    jsonArray = new JSONArray(location);
                    for(int i = 0;i < jsonArray.length();i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        if(jsonObj.getString("id") != null) {
                            locationID = jsonObj.getString("id");
                        }
                    }
                    // run: 101220305
                }catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"网络连接失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private JSONArray jsonArray;
    private void queryProvinces() {
        titleText.setText(getString(R.string.china));
        backButton.setVisibility(View.GONE);
        // LitePal api
        // 后端优化
        // provinceList = LitePal.findAll(Province.class);
        HttpUtil.sendOkHttpRequest(Constant.QUERY_PROVINCES, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = response.body().string();
                try {
                    jsonArray = new JSONArray(str);
                    dataList.clear();
                    for(int i = 0;i < jsonArray.length();i++){
                        Province province = new Province();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        province.setProvinceName(jsonObject.getString("cityname"));
                        province.setProvinceCode(jsonObject.getInt("pid"));
                        province.setId(jsonObject.getInt("id"));
                        dataList.add(province.getProvinceName());
                        provinceList.add(province);
                    }
                    listView.setSelection(0);
                    currentLevel = LEVEL_PROVINCE;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        /*if (provinceList.size() > 0) {
            dataList.clear();
            for (Province item : provinceList) {
                dataList.add(item.getProvinceName());
            }
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(Constant.BASE_URL, LEVEL_PROVINCE);
        }*/
    }


    // 查询城市
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        // 优化
        // 连接后端二级城市接口
        int pid = selectedProvince.getId();
        String strPid = String.valueOf(pid);

        Log.d(TAG, "queryCities: " + selectedProvince.getId());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(Constant.QUERY_CITIES + pid)
                            .get()
                            .build();

                    Response response = client.newCall(request).execute();

                    String string = response.body().string();
                    jsonArray = new JSONArray(string);

                    dataList.clear();
                    for(int i = 0;i < jsonArray.length();i++){
                        City city = new City();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        city.setCityName(jsonObject.getString("cityname"));
                        city.setCityCode(jsonObject.getInt("pid"));
                        city.setProvinceId(jsonObject.getInt("id"));
                        dataList.add(city.getCityName());
                        cityList.add(city);
                    }
                    adapter.notifyDataSetChanged();
                    listView.setSelection(0);
                    currentLevel = LEVEL_CITY;
                }catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                            currentLevel = LEVEL_CITY;
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);

        // 优化
        // 连接后端二级城市接口
        int pid = selectedCity.getProvinceId();

        Log.d(TAG, "queryCities: " + selectedCity.getProvinceId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(Constant.QUERY_COUNTIES + pid)
                            .get()
                            .build();

                    Response response = client.newCall(request).execute();

                    String string = response.body().string();
                    jsonArray = new JSONArray(string);
                    dataList.clear();
                    for(int i = 0;i < jsonArray.length();i++){
                        County county = new County();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        county.setCountyName(jsonObject.getString("cityname"));
                        county.setCityId(jsonObject.getInt("pid"));
                        county.setId(jsonObject.getInt("id"));
                        dataList.add(county.getCountyName());
                        countyList.add(county);
                    }
                    adapter.notifyDataSetChanged();
                    listView.setSelection(0);
                    currentLevel = LEVEL_COUNTY;
                }catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                            currentLevel = LEVEL_COUNTY;
                        }
                    });
                }
            }
        }).start();
    }
}
