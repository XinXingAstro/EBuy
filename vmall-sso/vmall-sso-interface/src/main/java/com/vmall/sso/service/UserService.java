package com.vmall.sso.service;

import com.vmall.common.pojo.VMallResult;
import com.vmall.pojo.TbUser;

public interface UserService {
    VMallResult checkData(String data, int type);
    VMallResult register(TbUser user);
    VMallResult login(String username, String password);
    VMallResult getUserByToken(String token);
}
