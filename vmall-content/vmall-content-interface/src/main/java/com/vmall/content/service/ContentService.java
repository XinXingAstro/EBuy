package com.vmall.content.service;

import com.vmall.common.pojo.VMallResult;
import com.vmall.pojo.TbContent;

import java.util.List;

public interface ContentService {
    VMallResult addContent(TbContent content);
    List<TbContent> getContentByCid(long cid);
}
