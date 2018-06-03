package com.vmall.controller;

import com.vmall.common.pojo.VMallResult;
import com.vmall.content.service.ContentService;
import com.vmall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理Controller
 */
@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/save")
    @ResponseBody
    public VMallResult addContent(TbContent content) {
        VMallResult result = contentService.addContent(content);
        return result;
    }
}
