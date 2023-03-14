package com.example.weather_server.controller;

import com.example.weather_server.model.City;
import com.example.weather_server.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:TODO
 * @author:Povlean
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/searchCities/{pid}")
    public List<City> searchCitysInProvince(@PathVariable Integer pid) {
        List<City> cityList = cityService.searchCitysByPid(pid);
        return cityList;
    }

    @GetMapping("/searchProvinces")
    public List<City> searchProvinces() {
        return cityService.queryProvinces();
    }

    @GetMapping("/search")
    public List<City> searchAllCities() {
        List<City> cities = cityService.searchAll();
        return cities;
    }

    @GetMapping("/searchCounties/{pid}")
    public List<City> searchCounties(@PathVariable Integer pid) {
        return cityService.queryCounties(pid);
    }

}
