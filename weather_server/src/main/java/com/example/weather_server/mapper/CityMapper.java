package com.example.weather_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.weather_server.model.City;
import org.apache.ibatis.annotations.Mapper;


/**
* @author Asphyxia
* @description 针对表【city】的数据库操作Mapper
* @createDate 2022-12-20 14:00:33
* @Entity generator.domain.City
*/
@Mapper
public interface CityMapper extends BaseMapper<City> {
}




