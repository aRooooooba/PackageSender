package com.nothing.arooba.packagesender;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Package {

    // 目标网站
    private String url;
    // 请求类型
    private String type;
    //网址参数，a=b&c=d&……&x=y
    private String params;
    //请求头
    private String headers;
    //请求体
    private String body;

    public Package(String url, String type, String params, String headers, String body) {
        this.url = url;
        this.type = type.toUpperCase();
        this.params = params;
        this.headers = headers;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String getWholeUrl() {
        String tmp = url;
        if (!url.contains("?")) {
            tmp = tmp.concat("?");
        } else if (!tmp.endsWith("&")) {
            tmp = tmp.concat("&");
        }
        return tmp.concat(params);
    }

    public boolean sendPackage() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request.Builder requestBuilder = new Request.Builder().url(getWholeUrl());
            if (!headers.isEmpty()) {
                for (String pair : headers.split("&")) {
                    requestBuilder = requestBuilder.addHeader(pair.split("=")[0], pair.split("=")[1]);
                }
            }
            if (type.equals("POST") && !body.isEmpty()) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (String pair : body.split("&")) {
                    formBodyBuilder = formBodyBuilder.add(pair.split("=")[0], pair.split("=")[1]);
                }
                requestBuilder = requestBuilder.post(formBodyBuilder.build());
            }
            client.newCall(requestBuilder.build()).execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        Package pck = new Package("http://10.3.8.217/login",
                "POST", "", "",
                "line=__none__&pass=jiaxiran&user=2015211409");
        if (pck.sendPackage()) {
            System.out.println("成功");
        }
    }
}
