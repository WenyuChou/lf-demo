package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.longfor.longhub.openapi.isvsdk.calltemplate.OpenApiRestTemplate;
import com.longfor.longhub.openapi.isvsdk.exception.OpenApiSdkException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouwenyu
 * date 2022-08-04
 */
@Slf4j
public class DemoForSyt {

    private final static String APP_ID = "167";
    private final static String SERVER_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTW170dPTgkYVufbIzdD2LBaPHGeG+WaGCluglL1iGG3OH7er+j3945tKwzd02oVdWGt8wl+C8n/L8C0/6ZDo6P3GbejpetDqVHgOh5CMjxj45ueacvH5vbPRrSZGymJjFiVZgdh9clUwOJwvdjWEvR+cSi3HS8/ofgrrpw5DdkwIDAQAB";
    private final static String CLIENT_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIGRGzaPhwHLq5xXqlZY8RgynKDLx8GQM6oj417HZAC9bUu++sf8PqwIjRXQxeGlnOEuUw/rwVP318QjCGAY1Cr9A72Gnp57S5hg/6qlevbHJxwyHvYUwIpm6CNjPbaWLrF8V0Z8JheYizpihD+HXooJTH5jWY9GKpG7zmsjkuIfAgMBAAECgYBAyM5jlJZ71lvDe64HKMe8QqpAWERUS4cZvoIUxPwCxrScmCyKzFyF5mytCMu4zPdJPMF5h3cORjGTt7SbYC7NZKthNwOLIGTpzupDBLar4MSXDG5Wt9XYhzTk4wHR7SUnoYvRLTW5OQx39ojVZBP64CuPrG6iaoEzz4eAixk+iQJBAPlmMvZb/ThXpk/fSm50edzp6SByVjGTOdheZSAoDZaiwLGyGXyqdqEfTntUpMJeX2lZDEkgmZ/ly/V3O7HydOUCQQCE/vwIlixT/buV5TLPbersgD7piinbwD6ukqYBeMJbaw9/AN7WCib81H8QB7E7AIC2+mRcV9O2QWkrmcTHQi6zAkEA3IT9MtCLyjHWIHGzgo8C4c8CD2qJxDQFxcdRa2l++l7WO0jbOFwRoM3puytQs3x2qNPQBSkKLVQnT9gptGJJ8QJAWg0o7Et6slhQprstf0RG2GIxtwIFO7Vc++lyG/b/atUABhe5yqXPJkxfyKPcvj2l6b97KweQ44xAwPP6SI0ofwJAIW/wyZJY3IOBJbE8hApP3ZtJ+U1q3I7lMSUFJPOUBwVLFlxqfvTwwYlH0IV9HxNcZvSb80AOvY50Ad5ALI1yow==";
    private String gaiaKey = "269bf5a6-8a1f-4873-b431-1b74c0e72a5e";
    private String url = "https://testapi.longfor.com/longem-gateway-uat/grant/recharge";

    /**
     * 注意：RestTemplate实例由调用方定义并作为参数传入, 此处仅做示例，请使用方注意调优
     */
    private RestTemplate restTemplate = new RestTemplate();

    private OpenApiRestTemplate openApiRestTemplate = new OpenApiRestTemplate(
            restTemplate,
            APP_ID,
            SERVER_PUBLIC_KEY,
            CLIENT_PRIVATE_KEY
    );

    public void postForEntity() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        //headers参数不变
        headers.put("X-longfor-app-id", Collections.singletonList(APP_ID));
        headers.put("X-safety-source", Collections.singletonList("openApiSdk"));
        headers.put("X-Gaia-Api-Key", Collections.singletonList(gaiaKey));
        //以下为业务参数
        Map<String, Object> param = new HashMap<>();
        //具体参数自行添加
        param.put("num", "蒙ZZB837");
        HttpEntity<Object> requestEntity = new HttpEntity<>(param, headers);

        try {
            System.out.println(JSON.toJSONString(requestEntity));
            Object o = openApiRestTemplate.postForEntity(url, requestEntity, Object.class);
            System.out.println("##########################");
            System.out.println(JSON.toJSONString(o));
        } catch (OpenApiSdkException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DemoForSyt test = new DemoForSyt();
        test.postForEntity();
    }
}
