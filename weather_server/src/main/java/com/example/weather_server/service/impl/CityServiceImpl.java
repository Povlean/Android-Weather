package com.example.weather_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.weather_server.mapper.CityMapper;
import com.example.weather_server.model.City;
import com.example.weather_server.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Asphyxia
* @description 针对表【city】的数据库操作Service实现
* @createDate 2022-12-20 14:00:33
*/
@Service
@Slf4j
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements CityService {

    @Autowired
    private CityMapper cityMapper;

    @Override
    public List<City> searchCitysByPid(Integer pid) {
        LambdaQueryWrapper<City> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(pid >= 0,City::getPid,pid);
        List<City> cityList = this.list(queryWrapper);
        return cityList;
    }

    @Override
    public List<City> searchAll() {
        List<City> cityList = this.list();
        return cityList;
    }

    @Override
    public List<City> queryProvinces() {
        LambdaQueryWrapper<City> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(City::getType,1);
        List<City> provinces = this.list(queryWrapper);
        return provinces;
    }

    @Override
    public List<City> queryCities(Integer pid) {
        if (pid < 0){
            return null;
        }
        LambdaQueryWrapper<City> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(City::getType,2).eq(City::getPid,pid);
        List<City> cities = this.list(queryWrapper);
        return cities;
    }

    @Override
    public List<City> queryCounties(Integer pid) {
        if (pid < 0){
            System.out.println("pid小于0");
            return null;
        }
        LambdaQueryWrapper<City> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(City::getType,3).eq(City::getPid,pid);
        List<City> counties = this.list(queryWrapper);
        return counties;
    }

}




