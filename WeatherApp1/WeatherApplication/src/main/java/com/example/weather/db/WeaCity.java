package com.example.weather.db;

public class WeaCity {
    private Integer id;
    private Integer pid;
    private String cityname;
    private Integer type;

    public WeaCity(Integer id, Integer pid, String cityname, Integer type) {
        this.id = id;
        this.pid = pid;
        this.cityname = cityname;
        this.type = type;
    }

    public WeaCity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WeaCity{" +
                "id=" + id +
                ", pid=" + pid +
                ", cityname='" + cityname + '\'' +
                ", type=" + type +
                '}';
    }
}
