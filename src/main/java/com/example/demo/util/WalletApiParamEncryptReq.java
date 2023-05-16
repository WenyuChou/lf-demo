package com.example.demo.util;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WalletApiParamEncryptReq {

    private String appId;

    private String encryptKey;

    private String encryptData;

}
