package com.vmall.search.dao;

import com.vmall.common.pojo.SearchItem;
import com.vmall.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询索引库商品Dao
 */
@Repository
public class SearchDao {

    @Autowired
    private HttpSolrClient.Builder builder;

    public SearchResult search(SolrQuery query) throws Exception {
        SolrClient solrClient = builder.build();
        //根据query对象进行查询
        QueryResponse response = solrClient.query(query);
        //取查询结果
        SolrDocumentList solrDocumentList = response.getResults();
        //取查询结果的总记录数
        long numFound = solrDocumentList.getNumFound();
        SearchResult result = new SearchResult();
        result.setRecordCount(numFound);
        List<SearchItem> itemList = new ArrayList<>();
        //把查询结果封装到searchItem对象中
        for (SolrDocument solrDocument : solrDocumentList) {
            SearchItem item = new SearchItem();
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            item.setId((String) solrDocument.get("id"));
            item.setImage((String) solrDocument.get("item_image"));
            item.setPrice((Long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            item.setTitle(title);
            //添加到商品列表
            itemList.add(item);
        }
        //把结果添加到SearchResult中
        result.setItemList(itemList);
        //返回
        return result;
    }
}
