package com.vmall.solrj;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.ArrayList;


public class TestSolrCloud {
    @Test
    public void testSolrCloudAddDocument() throws Exception {
//        String zkHost = "192.168.3.60:2181,192.168.3.60:2182,192.168.3.60:2183";
//        CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder().withZkHost(zkHost).build();
        //创建一个CloudSolr对象，构造方法中需要指定zookeeper的地址列表
        ArrayList<String> solrUrls = new ArrayList<>();
        solrUrls.add("http://192.168.3.61:8180/solr");
        solrUrls.add("http://192.168.3.61:8280/solr");
        solrUrls.add("http://192.168.3.62:8180/solr");
        solrUrls.add("http://192.168.3.62:8280/solr");
        CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder(solrUrls).build();
        //需要设置一个默认的collection
        cloudSolrClient.setDefaultCollection("collection01");
        //创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域
        document.addField("id", "test002");
        document.addField("item_title", "测试商品名称");
        document.addField("item_price", 10000);
        //把文档写入索引库
        cloudSolrClient.add(document);
        //提交
        cloudSolrClient.commit();
    }
}
