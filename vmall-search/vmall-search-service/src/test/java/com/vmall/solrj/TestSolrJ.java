package com.vmall.solrj;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestSolrJ {
    final String solrURL = "http://192.168.3.60:8080/solr/vmall";

    @Test
    public void testAddDocument() throws Exception {
        //创建一个SolrServer对象。创建一个HttpSolrServer对象
        //需要指定slor服务的url
//        String solrURL = "http://192.168.3.60:8080/solr/vmall";
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder(solrURL);
        SolrClient client = builder.build();
        //创建一个文档对象SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域，必须有id域，域的名称必须在manage.schema中定义
        document.addField("id", "1234");
        document.addField("item_title", "测试商品2");
        document.addField("item_price", 1000);
        //把文档对象写入索引库
        client.add(document);
        //提交
        client.commit();
    }

    @Test
    public void deletedocumentById() throws Exception {
//        String solrURL = "http://192.168.3.60:8080/solr/vmall";
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder(solrURL);
        SolrClient client = builder.build();
        client.deleteById("test001");
        //提交
        client.commit();
    }

    @Test
    public void deleteDocumentByQuery() throws Exception {
//        String solrURL = "http://192.168.3.60:8080/solr/vmall";
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder(solrURL);
        SolrClient client = builder.build();
        client.deleteByQuery("item_title:测试商品2");
        client.commit();
    }

    @Test
    public void searchDocument() throws Exception {
        //创建一个SolrClient对象
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder(solrURL);
        SolrClient client = builder.build();
        //创建一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        //设置查询条件、过滤条件、分页条件、排序条件、高亮
//        query.set("q", "*:*");
        query.setQuery("手机");
        //分页条件
        query.setStart(0);
        query.setRows(10);
        //设置默认搜索域
        query.set("df", "item_keywords");
        //设置高量
        query.setHighlight(true);
        //设置高亮显示域
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<div>");
        query.setHighlightSimplePost("</div>");
        //执行查询，得到一个Response对象
        QueryResponse response = client.query(query);
        //取查询结果
        SolrDocumentList solrDocumentList = response.getResults();
        //取查询结果的总记录数
        System.out.println("查询结果总记录数： " + solrDocumentList.getNumFound());
        for (SolrDocument solrDocument : solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = "";
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));
            System.out.println("================================");
        }

    }
}
