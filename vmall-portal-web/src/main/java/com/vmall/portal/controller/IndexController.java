package com.vmall.portal.controller;

import com.vmall.common.utils.JsonUtils;
import com.vmall.content.service.ContentService;
import com.vmall.pojo.TbContent;
import com.vmall.portal.pojo.AD1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页展示Controller
 */
@Controller
public class IndexController {
    @Value("${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;

    @Value("${AD1_WEIDTH}")
    private Integer AD1_WEIDTH;

    @Value("${AD1_WEIDTH_B}")
    private Integer AD1_WEIDTH_B;

    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;

    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;



    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String showIndex(Model model) {
        //根据cid查询轮播图内容列表
        List<TbContent> contentList = contentService.getContentByCid(AD1_CATEGORY_ID);
        //把列表转换为AD1Node列表
        List<AD1Node> ad1Nodes = new ArrayList<>();
        for (TbContent tbContent : contentList) {
            AD1Node node = new AD1Node();
            node.setAlt(tbContent.getTitle());
            node.setHeight(AD1_HEIGHT);
            node.setHeightB(AD1_HEIGHT_B);
            node.setWidth(AD1_WEIDTH);
            node.setWidthB(AD1_WEIDTH_B);
            node.setSrc(tbContent.getPic());
            node.setSrcB(tbContent.getPic2());
            node.setHref(tbContent.getUrl());
            //添加到节点列表
            ad1Nodes.add(node);
        }
        //把列表转换成json数据
        String ad1Json = JsonUtils.objectToJson(ad1Nodes);
        //把json数据传递给页面
        model.addAttribute("ad1", ad1Json);
        return "index";
    }
}
