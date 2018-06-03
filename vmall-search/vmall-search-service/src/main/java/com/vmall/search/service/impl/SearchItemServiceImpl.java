package com.vmall.search.service.impl;

import com.vmall.common.pojo.SearchItem;
import com.vmall.common.pojo.VMallResult;
import com.vmall.search.mapper.SearchItemMapper;
import com.vmall.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品数据导入索引服务
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private CloudSolrClient.Builder builder;
//    private HttpSolrClient.Builder builder;

    @Override
    public VMallResult importItemsToIndex() {
        try {
            CloudSolrClient client = builder.build();
            client.setDefaultCollection("collection01");
            //1.查询所有商品数据
            List<SearchItem> itemList = searchItemMapper.getItemList();
            //2.遍历商品数据添加到索引库
            for (SearchItem searchItem : itemList) {
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                //向文档中添加域
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategory_name());
                document.addField("item_desc", searchItem.getItem_desc());
                //把文档写入索引库
                client.add(document);
            }
            //3.提交
            client.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return VMallResult.build(500, "数据导入失败");
        }

        //4.返回添加成功
        return VMallResult.ok();
    }
}
