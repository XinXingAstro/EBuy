package com.vmall.manager.service;

import com.vmall.common.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    List<EasyUITreeNode> getItemCatList(long parentId);
}
