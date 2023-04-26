package com.bing.mini.controller;

import cn.hutool.core.io.resource.ResourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ServletContext servletContext;

    @RequestMapping("/login1")
    public String child() {
        try {
            String path =   ResourceUtils.getFile("classpath:templates/login.html").getPath();
            System.out.println("path: " + path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "login";
    }
}
