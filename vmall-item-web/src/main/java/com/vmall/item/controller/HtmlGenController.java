package com.vmall.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 网页静态化处理Controller
 */
@Controller
public class HtmlGenController {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genhtml")
    @ResponseBody
    public String genHtml() throws Exception {
        //生成静态页面
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap();
        data.put("hello","spring freemarker test");
        FileWriter fileWriter = new FileWriter("/Users/xinxing/Desktop/out/test.html");
        template.process(data, fileWriter);
        fileWriter.close();
        // 返回结果
        return "OK";
    }
}
