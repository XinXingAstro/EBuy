package com.vmall.search.mapper;

import com.vmall.common.pojo.SearchItem;

import java.util.List;

public interface SearchItemMapper {
    List<SearchItem> getItemList();
    SearchItem getItemById(long itemId);
}
