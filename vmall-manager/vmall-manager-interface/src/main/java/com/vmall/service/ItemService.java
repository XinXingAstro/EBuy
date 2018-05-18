package com.vmall.service;

import com.vmall.common.pojo.EasyUIDataGridResult;
import com.vmall.common.pojo.VMallResult;
import com.vmall.pojo.TbItem;

public interface ItemService {

    TbItem getItemById(long itemId);
    EasyUIDataGridResult getItemList(int page, int rows);
    VMallResult addItem(TbItem item, String desc);
}
