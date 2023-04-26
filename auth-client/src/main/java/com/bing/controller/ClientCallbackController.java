package com.bing.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
public class ClientCallbackController {

    @Value("${oauth.localIp")
    String localIp;

    @Value("${oauth.serverIpAndPort")
    String serverIpAndPort;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/gateway/oAuthCallback")
    public String getToken(@RequestParam String code, @RequestParam(required = false) String state) {
        log.info("receive code {}", code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_id", "client1");
        params.add("client_secret", "secret");
        String callbackUrl = "http://" + localIp + ":80/gateway/oAuthCallback";
        params.add("redirect_uri", callbackUrl);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        try{
            String serverTokenUrl = "http://" + serverIpAndPort + "/oauth/token";
            ResponseEntity<String> response = restTemplate.postForEntity(serverTokenUrl, requestEntity, String.class);
            String token = response.getBody();
            log.info("token => {}", token);
            JSONObject tokenObject = JSONObject.parseObject(token);
            if(tokenObject == null){
                return "error";
            }
            String redirectPersonUrl = "http://" + serverIpAndPort + "#/outsystem?type=baqrsrygl?access_token=" + tokenObject.getString("access_token");
            log.info("ClientCallbackController.getToken:url = " + redirectPersonUrl);
            return "redirect:" + redirectPersonUrl;
        }catch (Exception e){
            log.error("e: " + e);
        }
        return "error";
    }
}

//http://localhost:8080/oauth/authorize?client_id=client1&response_type=code&redirect_uri=http://localhost:80/gateway/oAuthCallback