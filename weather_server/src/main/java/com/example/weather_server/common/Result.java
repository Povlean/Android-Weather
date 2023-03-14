package com.example.weather_server.common;

import lombok.Data;

import java.util.List;

/**
 * @description:TODO
 * @author:Povlean
 */
@Data
public class Result {
    private Integer code;
    private List data;
    private String msg;

    public Result() {

    }

    public Result(Integer code, List data) {
        this.code = code;
        this.data = data;
    }

    public Result(Integer code, List data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
