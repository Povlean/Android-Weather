package com.example.weather.util;

import android.util.Log;

import androidx.annotation.NonNull;

import org.apache.http.params.HttpParams;
import org.litepal.util.Const;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CityTest {
    private final static String TAG = "CityTest";
    public static void queryProvince() {
        HttpUtil.sendOkHttpRequest(Constant.BASE_URL, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "queryProvince: exception " +e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "queryProvince:  response=" + response.body().string());
            }
        });
    }

    public static void queryCity(int provinceId) {
        String url = Constant.BASE_URL + "/" + provinceId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "queryCity: exception " +e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "queryCity:  " + response.body().string());
            }
        });
    }

    public static void queryCounty(int provinceId, int cityId) {
        String url = Constant.BASE_URL + "/" + provinceId + "/" + cityId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "queryCounty: exception " +e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "queryCounty:  " + response.body().string());
            }
        });
    }


}
