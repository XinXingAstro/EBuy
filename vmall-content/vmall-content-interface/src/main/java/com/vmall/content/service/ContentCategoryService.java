package com.vmall.content.service;

import com.vmall.common.pojo.EasyUITreeNode;
import com.vmall.common.pojo.VMallResult;

import java.util.List;

public interface ContentCategoryService {

    List<EasyUITreeNode> getContentCategoryList(long parentId);
    VMallResult addContentCategory(Long parentId, String name);
}
