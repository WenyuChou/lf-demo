package com.example.demo.util;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class longCornAesTest {
    private static String ip = "http://localhost:8511";
    @SneakyThrows
    public static void main1(String[] args) {
        //String[] strings = longCornAesTest.modifyActivity();
        String[] strings = longCornAesTest.getML();
        //String[] strings = longCornAesTest.disable(1);
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/json");
        head.put("X-Gaia-Api_key","7fc412f9-d959-488d-9cff-dc48949cdc8a");
        head.put("X-lc-op-loginKey", "TGT-609-M-R-jgAgYXFP--g9zufWYu0zF2J2RZW3sqC6QWNVG5lYmzCIg5HG0K69F568XJMjjSk-longhu");
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, "longcoinlongcoin".getBytes(), "longcoinlongcoin".getBytes());
        String paramHex = aes.encryptHex(strings[1]);
        String result = HttpClient.sendHttp(strings[0], paramHex, head, "GET", 10)[1];
        log.info("············入参············");
        System.out.println(strings[1]);
        log.info("············出参············");
        System.out.println(aes.decryptStr(result, CharsetUtil.CHARSET_UTF_8));
    }

    public static String[] selectPage() {
        String url = ip + "/redPackActivity/list";
        JSONObject json = new JSONObject();
        json.put("pageNum", 1);
        json.put("pageSize", 20);
        String param = json.toString();
        return new String[]{url, param};
    }

    public static String[] modifyActivity() {
        String url = ip+ "/redPackActivity/modifyActivity";
        JSONObject json = new JSONObject();
        json.put("id", 19);
        json.put("activityName", "521hd");
        json.put("redPackTop", "66");
        json.put("startTime", "2021-05-20 00:00:00");
        json.put("endTime", "2021-05-22 23:59:59");
        String param = json.toString();
        return new String[]{url, param};
    }

    public static String[] disable(int id) {
        String url = ip + "/redPackActivity/disable/" + id;
        JSONObject json = new JSONObject();
        String param = json.toString();
        return new String[]{url, param};
    }
    public static String[] getML(){
        String url = "http://lc-uat.longfor.uat:8089/proxy/gf-lc-op-endpoint//administrators/getMenuList";
        JSONObject json = new JSONObject();
        String param = json.toString();
        return new String[]{url, param};
    }
    public static void readAesResponse(){
        String res = "c6227d56b8c101ea343d6b5d69402ce6b9fbc4405687125cbc066846652d09473c03b521f66959479dad479ea4bb197beed138839a657d4d538c4fa97f3b8c32f10589a8f590f8f454c16c0cc93897065d7f0eaa1eb9b065bd508a4b56e9ae09733473388cebb777397a7db31e93107c95a02ab0fe5a2bfafeaf712dad3bb186bc8a531b66dc52b5b5d31fae61ead4212c02bd2e74a5e2bc9cfd56299d64af21";
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, "longcoinlongcoin".getBytes(), "longcoinlongcoin".getBytes());
        System.out.println(aes.decryptStr(res, CharsetUtil.CHARSET_UTF_8));
    }

    public static void main(String[] args) {
        readAesResponse();
    }
}
