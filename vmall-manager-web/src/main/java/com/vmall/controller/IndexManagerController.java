package com.vmall.controller;

import com.vmall.common.pojo.VMallResult;
import com.vmall.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 索引库回库Controller
 */
@Controller
public class IndexManagerController {
    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("index/import")
    @ResponseBody
    public VMallResult importIndex() {
        VMallResult result = searchItemService.importItemsToIndex();
        return result;
    }
}
