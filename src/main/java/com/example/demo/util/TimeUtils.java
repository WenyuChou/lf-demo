package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class TimeUtils {

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static long getDateLong(String dateStr){
        return getDate(dateStr).getTime();
    }

    public static String getDateStr(Date data){
        return new SimpleDateFormat(TIME_FORMAT).format(data);
    }

    public static Date getDate(String dateStr){
        try {
            return new SimpleDateFormat(TIME_FORMAT).parse(dateStr);
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        return new Date(0L);
    }
    public static void main(String[] args) {
        System.out.println(Long.valueOf(""));
    }
}
