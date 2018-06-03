package com.vmall.search.service;

import com.vmall.common.pojo.SearchResult;

public interface SearchService {
    SearchResult search(String queryString, int page, int rows) throws Exception ;
}
