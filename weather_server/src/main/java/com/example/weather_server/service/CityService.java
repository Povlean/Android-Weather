package com.example.weather_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.weather_server.model.City;

import java.util.List;


/**
* @author Asphyxia
* @description 针对表【city】的数据库操作Service
* @createDate 2022-12-20 14:00:33
*/
public interface CityService extends IService<City> {
    public List<City> searchCitysByPid(Integer id);

    public List<City> searchAll();

    public List<City> queryProvinces();

    public List<City> queryCities(Integer pid);

    public List<City> queryCounties(Integer pid);


}
