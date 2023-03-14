package com.example.weather.bean;

public class AirQuality {
    private String aqi;
    private String pm25;

    public AirQuality(String aqi, String pm25) {
        this.aqi = aqi;
        this.pm25 = pm25;
    }

    @Override
    public String toString() {
        return "AirQuality{" +
                "aqi='" + aqi + '\'' +
                ", pm25='" + pm25 + '\'' +
                '}';
    }

    public AirQuality() {
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
}
