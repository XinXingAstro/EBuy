package com.vmall.controller;

import com.vmall.common.pojo.EasyUITreeNode;
import com.vmall.common.pojo.VMallResult;
import com.vmall.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理Controller
 */
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(
            @RequestParam(value = "id", defaultValue = "0") long parenId) {
        List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parenId);
        return list;
    }

    @RequestMapping("/content/category/create")
    @ResponseBody
    public VMallResult addContentCategory(Long parentId, String name) {
        VMallResult result = contentCategoryService.addContentCategory(parentId, name);
        return result;
    }
}
