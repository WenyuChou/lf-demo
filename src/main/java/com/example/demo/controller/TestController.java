package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private RestTemplate restTemplate;

    @PostMapping("/getTest/{str}")
    public String getTest(@PathVariable("str") String str){
        log.info("requestParam:{}",str);
        return "success";
    }

    @PostMapping("/postTest")
    public String postTest(@RequestBody String str){
        HttpEntity<String> httpEntity = new HttpEntity<>("");
        JSONObject jsonObject = restTemplate.postForObject("http://127.0.0.1/8981", httpEntity, JSONObject.class);
        return jsonObject.toJSONString();
    }
    @PostMapping("/jsonTest")
    public String jsonTest(@RequestBody(required = false) User str){
        return JSONObject.toJSONString(str);
    }
}
