package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static String loginAndGetResponse(String username, String password) {
        HttpRequest httpRequset = HttpRequest.post("http://47.91.156.35:8000/auth/login")
                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.36")
                .send(getJsonBodyByUserNameAndPassword(username, password));

        String sessionId = httpRequset.headers().get("Set-Cookie").stream()
                .filter(v -> v.startsWith("JSESSIONID"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no session id in cookie"));
        return HttpRequest.get("http://47.91.156.35:8000/auth")
                .header("Cookie", sessionId)
                .body();
    }

    private static String getJsonBodyByUserNameAndPassword(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        return JSON.toJSONString(body);
    }

    public static void main(String[] args) {
        loginAndGetResponse("xdml", "xdml");
    }
}
