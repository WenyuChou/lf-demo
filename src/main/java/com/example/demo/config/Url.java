package com.example.demo.config;


import lombok.Getter;
import lombok.Setter;

public enum Url {

    sit_recharge("http://api.longfor.sit/lf-wallet-trading-endpoint-sit/api/terrace/createAdRecharge/Encrypt","b47f6962-ba82-4dc7-a673-ffb934449fd2"),
    uat_recharge("http://api.longfor.uat/lf-wallet-trading-endpoint-uat/api/terrace/createAdRecharge/Encrypt","6812e8b6-7dc3-4334-b7b3-6c19b26b6147");

    @Getter
    @Setter
    private String url;
    @Getter
    @Setter
    private String headKey;

    Url(String url,String headKey){
        this.url = url;
        this.headKey = headKey;
    }

}
