package com.vmall.content.service.impl;

import com.vmall.common.pojo.VMallResult;
import com.vmall.content.service.ContentService;
import com.vmall.mapper.TbContentMapper;
import com.vmall.pojo.TbContent;
import com.vmall.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Override
    public VMallResult addContent(TbContent content) {
        //补全pojo的属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入到内容表
        contentMapper.insert(content);
        return VMallResult.ok();
    }

    @Override
    public List<TbContent> getContentByCid(long cid) {
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        //执行查询
        List<TbContent> list = contentMapper.selectByExample(example);
        return list;
    }
}
