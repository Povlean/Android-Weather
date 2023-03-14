package com.example.weather_server;

import com.example.weather_server.model.City;
import com.example.weather_server.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class WeatherServerApplicationTests {

    @Autowired
    private CityService cityService;

    @Test
    public void testSearch() {
        int pid = 3;
        List<City> cities = cityService.searchCitysByPid(pid);
        System.out.println(cities);
    }

    @Test
    public void testSearch02() {
        List<City> cities = cityService.searchAll();
        for (City city : cities) {
            System.out.println(city);
        }
    }

    @Test
    public void searchProvinces() {
        List<City> provinces = cityService.queryProvinces();
        for (City province : provinces) {
            String proName = province.getCityname();
            System.out.println(proName);
        }
    }

    @Test
    public void searchCities() {
        Integer pid = 6;
        List<City> cities = cityService.queryCities(pid);
        for (City city : cities) {
            System.out.println(city);
        }
    }

    @Test
    public void searchCounties() {
        Integer pid = 46;
        List<City> counties = cityService.queryCounties(pid);
        for (City county : counties) {
            System.out.println(county);
        }
    }

    @Test
    public void searchThirdCities() {
        cityService.queryProvinces();
    }

}
