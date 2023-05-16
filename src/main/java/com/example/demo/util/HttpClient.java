package com.example.demo.util;


import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Wenyu Zhou
 */
public class HttpClient {

    public static boolean print = true;

    public static String[] sendHttp(String url, String param, Map<String, String> headers, String method, Integer timeOut) {
        method = method == null ? "GET" : method.toUpperCase();
        PrintWriter out;
        BufferedReader in;
        String result = "";

        if (!StringUtils.isEmpty(param) && "GET".equalsIgnoreCase(method)) {
            url = url + "?" + param;
        }
        int responseCode;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) conn;
            if (timeOut != null && timeOut > 0) {
                httpURLConnection.setReadTimeout(timeOut * 1000);
            }
            httpURLConnection.setRequestMethod(method);
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpURLConnection.setRequestProperty(header.getKey(), header.getValue());
            }
            httpURLConnection.setDoInput(true);
            boolean bool = !StringUtils.isEmpty(param) &&
                    ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method));
            if (bool) {
                httpURLConnection.setDoOutput(true);
                out = new PrintWriter(httpURLConnection.getOutputStream());
                out.print(param);
                out.flush();
            }
            httpURLConnection.connect();
            /*
             * 判断getResponseCode，
             * 当返回不是HttpURLConnection.HTTP_OK， HttpURLConnection.HTTP_CREATED， HttpURLConnection.HTTP_ACCEPTED 时，
             * 不能用getInputStream()，而是应该用getErrorStream()
             */
            responseCode = httpURLConnection.getResponseCode();
            //System.out.println("content-type:" + httpURLConnection.getContentType());
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
            responseCode = HttpsURLConnection.HTTP_INTERNAL_ERROR;
        }
        if(print){
            System.out.println("http 请求 url = " + url + " ; result = " + result);
        }
        return new String[]{String.valueOf(responseCode), result};
    }

    public static String[] sendHttps(String url, String param, Map<String, String> headers, String method, Integer timeOut) {
        method = method == null ? "GET" : method.toUpperCase();
        PrintWriter out;
        BufferedReader in;
        String result = "";
        if (!StringUtils.isEmpty(param) && "GET".equalsIgnoreCase(method)) {
            url = url + "?" + param;
        }
        int responseCode;
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            URL realUrl = new URL(null, url, new sun.net.www.protocol.https.Handler());
            URLConnection conn = realUrl.openConnection();
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) conn;
            if (timeOut != null && timeOut > 0) {
                httpsURLConnection.setReadTimeout(timeOut * 1000);
            }
            httpsURLConnection.setRequestMethod(method);
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpsURLConnection.setRequestProperty(header.getKey(), header.getValue());
            }

            httpsURLConnection.setDoInput(true);
            boolean bool = !StringUtils.isEmpty(param) && ("POST".equalsIgnoreCase(method)
                    || "PUT".equalsIgnoreCase(method));
            if (bool) {
                httpsURLConnection.setDoOutput(true);
                out = new PrintWriter(httpsURLConnection.getOutputStream());
                out.print(param);
                out.flush();
            }
            httpsURLConnection.connect();
            /*
             * 判断getResponseCode，
             * 当返回不是HttpURLConnection.HTTP_OK， HttpURLConnection.HTTP_CREATED， HttpURLConnection.HTTP_ACCEPTED 时，
             * 不能用getInputStream()，而是应该用getErrorStream()
             */
            responseCode = httpsURLConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(httpsURLConnection.getErrorStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
            responseCode = HttpsURLConnection.HTTP_INTERNAL_ERROR;
        }
        if(print){
            System.out.println("http_S 请求 url = " + url + " ; param = "+ param +" ; result = " + result);
        }
        return new String[]{String.valueOf(responseCode), result};
    }

    public static String[] doHttpRetry(String url, String param, String method, Integer timeOut, Integer retryTimes, Long retryInterval) {
        try {
            String[] result;
            result = doHttp(url, param, new HashMap<>(), method, timeOut);
            while (--retryTimes > 0 && Integer.parseInt(result[0]) != HttpsURLConnection.HTTP_OK) {
                Thread.sleep(retryInterval);
                result = doHttp(url, param, new HashMap<>(), method, timeOut);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{String.valueOf(HttpsURLConnection.HTTP_INTERNAL_ERROR), null};
        }
    }

    public static String[] doHttp(String url, String param, String method, Integer timeOut) {
        return doHttp(url, param, new HashMap<>(), method, timeOut);
    }

    public static String[] doHttp(String url, String param, Map<String, String> headers, String method, Integer timeOut) {
        headers.put("Content-Type", "application/json");
        return url.startsWith("https")
                ? sendHttps(url, param, headers, method, timeOut)
                : sendHttp(url, param, headers, method, timeOut);
    }

    public static class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            // TODO Auto-generated method stub
            return true;
        }
    }

    static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }};
}
