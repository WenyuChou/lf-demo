package com.example.demo.config;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum AppKeys {

    UAT_014("014",
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQcScXMhLar13f2A4iS8-6zZV77I17XEz3s5zexixA0kEMcbNZ9lE80Ghz-zdpoXnYU8_6X6IUBvfj3wWHB3FZktwzhMQ7tpC_60oDspaR8NqRXhwgq7OTZGZO5aGPlvs41hVYSHGGGjvXzUi-bffljLOnTGoWpp8sCgRKq_i7cQIDAQAB",
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANBxJxcyEtqvXd_YDiJLz7rNlXvsjXtcTPeznN7GLEDSQQxxs1n2UTzQaHP7N2mhedhTz_pfohQG9-PfBYcHcVmS3DOExDu2kL_rSgOylpHw2pFeHCCrs5NkZk7loY-W-zjWFVhIcYYaO9fNSL5t9-WMs6dMahamnywKBEqr-LtxAgMBAAECgYEApeInNfKHxrnhf482UFz9CLEr0d_ECPS_3DF5GmGkPkIVuo2SKno41KkXN5Yp10rG6T5qOdt55W5WQWbvwzJNJYgAwYOnFJsE04oRQtrqrGqkkxw1rGg4u10Y6d1sW4z_vd02bx87YqUnBWLY567yhHlV-q8VuOHCkmsM4yvNhzECQQDyKLpacxY2gdIxRAny55N7_NtY6pmyeACtsp8v_QXCzRHm7ak1Rasce2-9U5s99joxIZO7TF3lQ2xmyP7psUzDAkEA3FsThWm-8dOunFVtbYytlH2YzUsWP7YHWMY5s4fyhvIocPg6cbuuOXLXkiezYEEvsAWASpxMZqyjKUW12r0juwJBANIJ2XlSkVW9zJ7GsQprKlVEwMyiz6UoyQ3f_IsXH8QTMw_l2_pE72HI_uZseh6IT3kF8qlmV5rfnEqa09_G1RkCQHm4EWNrkx7vqA8RHT4bLDLO4d3csTCGMy61aGAkIzKphu9IHrh-THHNXwiLx4jO2TGpCEGuboF0sTf2Gsr2GUsCQATF5X_O3q2bADwlTUB6Sdpa1J6puCPiK7bbqkZHJydkR-UwCmwl3snVWtNmetjYwhE2eyrDqUFvewP06aU0_es",
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpOEb5vGe054rcK_aWfMGrfFS_YQ_ReKMtjdncv9NSWSor16NEyJSYV18BFtoTVg8ShL9K7u3k21fjVEHoTzu6Y4B35DBnuLkw7SvKcPodp0333GJu6r6TIw3hBMbw8z86cGo9OR9WIeNvjsZyRYMubKRHKJ6gdJGmLG2gV7iDwQIDAQAB"
    ),
    UAT_138(
            "138",
            "",
            "",
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpOEb5vGe054rcK_aWfMGrfFS_YQ_ReKMtjdncv9NSWSor16NEyJSYV18BFtoTVg8ShL9K7u3k21fjVEHoTzu6Y4B35DBnuLkw7SvKcPodp0333GJu6r6TIw3hBMbw8z86cGo9OR9WIeNvjsZyRYMubKRHKJ6gdJGmLG2gV7iDwQIDAQAB"
    ),
    UAT_202208041004708355475767296(
            "202208041004708355475767296",
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2SCjS6bJuxnyMsY3/w9C7EnEhEsa8TqKWRGICLAme5RR8a+qQmPuOqMz4tX5oxGlPoy6aeTkGwP/w6z7LM8pVG5COA0NNW03X4qZgCZu3QyY5AhtQ4fzfD70Dj8Gdyud2fI/+xXpn1LXzrBDmEX2ED8ZV7BdY4JuCPpD6I3xomwIDAQAB",
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALZIKNLpsm7GfIyxjf/D0LsScSESxrxOopZEYgIsCZ7lFHxr6pCY+46ozPi1fmjEaU+jLpp5OQbA//DrPsszylUbkI4DQ01bTdfipmAJm7dDJjkCG1Dh/N8PvQOPwZ3K53Z8j/7FemfUtfOsEOYRfYQPxlXsF1jgm4I+kPojfGibAgMBAAECgYEAkhRoKwbASYhl/ja47tTVrWYNJY0vmW+HWtd4l/cEysMZVNEDTVhcmSpsAZEpufpVz02eJoupiOEY+9D7JMvfhFCVcAzwqklmvlxfDfr/IsAYGonUoFFyjcb1W2mzgHT96hUjlz7TIZOwDd73jwuZ4ntN2Gb8oX9AcTGdZ8TIeVECQQD9JvvJxNa3AzlStqJ1zttElzeyMHrKCuru2ZVGzu/RJyDzYDFVzXoUEC4ichOZDLrTcbcOLwE71AI1yLjlzL7pAkEAuFUWJ2C5fxxR9AgopQj0gXx+mtfmLtwMNagpwLpcocE8SXyjXVi1ql/Cm6mpkybZKyvvzK6OTdF5vIvTt0Ig4wJAZ+5LTQjtBfHi5NxqUmd8iepzSUqx/qUA0tKtgIaQ2pNv6btCRfG3/uitqCKhfSJGyWxUNRSDMybVSXOAxYeLkQJBAKDqxphBvC3XD0Q2CyVnl+uiPvP2HiAyQQZmc2yoRE2BX5vXjtXip1TAnCdACaVIeLVTedf5WolcoSWT+5Mzy3kCQEzxn/tpsjV4PcnURfT2orSrLAf7npp83NzYKYrzJ5lQf+bU6ibJqokF/LoS4vOXoG77XPU30yp5ahH+HfLg6k0=",
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMIWw04S8Q4BK/6FruqIIYGwcPuLJLNZcfu0oybJ8+7AqwhySOcv2QXJ6sIeagJlj3UXIsYNQwRumijd+e/c6PBlfPTKCBsoaBGRwcWCxb693w6fTTD2TnKYYynx3GYHDWUu4tiyGNvDJuBrjiXrpn0MLPElMrg/oTCSBkDI3XCwIDAQAB"
    ),
    UAT_202210101029074038653190144(
            "202210101029074038653190144",
            "",
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALMzzMjU2xrogDLXo/GHrl+yQIHB1Zeo/GuVb7q0TkNhU26RGTa4y/9yxLyJXEHXnWSD9k7KYJF8cf3uuzrjwBMcjVGnOHNhCydTqxWN7AQTqU5DNWr0gCCOrBxlFd7wQRfj2EIcugqqaO3WLkyQrxdq/bYqj6Al6Eywr4pmBN7bAgMBAAECgYBOZPTLTsLjTJ74nvlrag3Qc2xg12kXRafXcLJGHSa3t1YGYpeDJuinQY/rYrPnYenK1SflZAdG3ggILG3f305g0cH1B3HL8kmCYwupm+Qzfgp0ESuHp5VQUjDVEzpuvcnvOU47fp9csjc4JMq+tbOKbqYz3gsUCgK/x+nXOF8qkQJBAOr/sjjlF7CD49xFBZegZXPAGu77Oa6i9vufNYM/AMErcKVk0Zce9ZcjvnJhZQWVsclZr3qvNbIPVsCh9wFAE6UCQQDDN5eGaMIJBYQuJKQnrO98OvkdMPzwGWrefeYBr2tDYi9wVZK7HqayQ6Vl4cR7vR8bYC1wAa9uBMTpF3WM5KB/AkEAtAsxXbvPuZJi6mkLTs85y0Qcnopl+DGtLr0c/7AugNEqXd1vYuv6V1CwYevSvUOiUdTfyAwjBIgR/EZa+18/bQJBAKoAj4QNVmxk0WcdoZarkyKseKRjNOIiQF7hZJuzF8Xmi0lYPB+X4bL8Fd1TbrgTSjOc3fTcgtlcbd5O/59iuiECQERB4HnMhPql6BUnnnkng6LYChHjpuBh2Cyq3lqcm4m+zbuI6g5j78rBnCbAL1BN/eDRj/azT2DV186usKRXDrE=",
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvsAnIzUVpMEBCmxH9Mrwu2Z4O0JzwyaQ84eyp0V+t2B71+8pRIZe/bZDd3Se8oUHqAVnZHKPpwubohVIAfW+3z32zvjBtC7esvGnudtMRcntNGmlYkFiGvwy5NZ4FomLbYi/+ZXmkbwmPd29y160uy1j4971kE6hIIon442772wIDAQAB"
    );

    AppKeys(String appId, String cPublicKey, String cPrivateKey, String bPublicKey) {
        this.appId = appId;
        this.cPublicKey = cPublicKey;
        this.cPrivateKey = cPrivateKey;
        this.bPublicKey = bPublicKey;
    }

    private final String appId;
    private final String cPublicKey;
    private final String cPrivateKey;
    private final String bPublicKey;

    private static final Map<String, AppKeys> map = new HashMap<>();

    static {
        map.put("014", UAT_014);
        map.put("202208041004708355475767296",UAT_202208041004708355475767296);
        map.put("202210101029074038653190144",UAT_202210101029074038653190144);
    }
    public static AppKeys getAppInfo(String appId){
        return map.get(appId);
    }
}
