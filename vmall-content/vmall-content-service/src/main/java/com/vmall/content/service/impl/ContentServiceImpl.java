package com.vmall.content.service.impl;

import com.vmall.common.pojo.VMallResult;
import com.vmall.common.utils.JsonUtils;
import com.vmall.content.service.ContentService;
import com.vmall.jedis.JedisClient;
import com.vmall.mapper.TbContentMapper;
import com.vmall.pojo.TbContent;
import com.vmall.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;

    @Override
    public VMallResult addContent(TbContent content) {
        //补全pojo的属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入到内容表
        contentMapper.insert(content);
        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
        return VMallResult.ok();
    }

    @Override
    public List<TbContent> getContentByCid(long cid) {
        //请求来了之后先查询缓存，判断缓存中有没有
        //查缓存不能影响系统正常业务逻辑，所以要用try catch
        try {
            //查询缓存
            String json = jedisClient.hget(INDEX_CONTENT, cid + "");
            //如果查询到结果，把json转换成list返回
            if (StringUtils.isNotBlank(json)) {
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //缓存中没有命中，需要查询数据库
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        //执行查询
        List<TbContent> list = contentMapper.selectByExample(example);
        //如果没有命中，在返回结果之前需要将结果添加到缓存
        //添加缓存不能影响正常业务逻辑，所有要用try catch
        try {
            jedisClient.hset(INDEX_CONTENT, cid+"", JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return list;
    }
}
