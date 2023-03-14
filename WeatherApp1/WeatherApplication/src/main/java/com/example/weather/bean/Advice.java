package com.example.weather.bean;

public class Advice {
    private String sportAdv;
    private String washAdv;
    private String clothAdv;

    public Advice(String sportAdv, String washAdv, String clothAdv) {
        this.sportAdv = sportAdv;
        this.washAdv = washAdv;
        this.clothAdv = clothAdv;
    }

    public Advice() {
    }

    public String getSportAdv() {
        return sportAdv;
    }

    public void setSportAdv(String sportAdv) {
        this.sportAdv = sportAdv;
    }

    public String getWashAdv() {
        return washAdv;
    }

    public void setWashAdv(String washAdv) {
        this.washAdv = washAdv;
    }

    public String getClothAdv() {
        return clothAdv;
    }

    public void setClothAdv(String clothAdv) {
        this.clothAdv = clothAdv;
    }

    @Override
    public String toString() {
        return "Advice{" +
                "sportAdv='" + sportAdv + '\'' +
                ", washAdv='" + washAdv + '\'' +
                ", clothAdv='" + clothAdv + '\'' +
                '}';
    }
}
