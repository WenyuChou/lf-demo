package com.example.demo.util;


import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouwenyu
 * date 2021-12-30
 */
@Slf4j
public class ShuangHu {

    public static String getLongforSign(String userName,String key) {
        String md5Str = DigestUtils.md5DigestAsHex(key.getBytes());
        String token = null;
        try {
            token = JWT.create()
                    .withIssuer("longfor")
                    .withClaim("userName", userName)
                    // 加密盐值 使用 28D1E3EA1456085C
                    .withClaim("key", md5Str)
                    .withExpiresAt(DateUtils.addSeconds(new Date(),36000))
                    //创建时间为当前时间减10秒，避免解密时出现异
                    .withIssuedAt(DateUtils.addSeconds(new Date(),-10))
                    // 使用了HMAC256加密算法。
                    // mysecret是用来加密数字签名的密钥。
                    .sign(Algorithm.HMAC256("09c47ce4-24d6-481b-9e98-5c280a8d17ac"));
        } catch (Exception e){
            log.error("JWT加密token抛出异常(Exception):{}",e.getMessage(),e);
        }
        return token;
    }
    public static void verifyToken(String token) throws Exception {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("09c47ce4-24d6-481b-9e98-5c280a8d17ac")).build();
        DecodedJWT jwt = verifier.verify(token);
        System.out.println(jwt.getIssuer());
        System.out.println(jwt.getClaim("userName").asString());
        System.out.println(jwt.getClaim("key").asString());
    }

    public static void main(String[] args) throws Exception{
        String url = "https://invest-uat-xjp.wu-capital.com/api/wucapInvestmentSubject/selectSubjectByNameAndNCcode";
        String key = "jwtKey:" + new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
        String token = getLongforSign("longfor", key);
        Map<String,String> heads = new HashMap<>(4);
        heads.put("Content-Type","application/json;charset=UTF-8");
        heads.put("token",token);
        JSONObject js = new JSONObject();
        js.put("appId","002");
        //js.put("ncCode","90001989");
        //js.put("name","西藏祥毓和泰企业管理有限公司");
        js.put("ncCode","0103");
        js.put("name","NC上海合毓投资管理有限公司");
        String[] result = HttpClient.doHttp(url, js.toJSONString(), heads, "POST", 10);
        System.out.println(js.toJSONString());
        System.out.println(result[0]);
        System.out.println(result[1]);
    }

    public static void main1(String[] args) {
        try {
            throw new Exception("内部的异常");
        } catch (Exception e) {
            log.info("catch住异常:{},{}", e.getMessage(), e.getStackTrace(),e);
        }
    }
}
