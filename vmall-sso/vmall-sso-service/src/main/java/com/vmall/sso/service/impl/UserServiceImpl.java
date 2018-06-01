package com.vmall.sso.service.impl;

import com.vmall.common.pojo.VMallResult;
import com.vmall.common.utils.JsonUtils;
import com.vmall.jedis.JedisClient;
import com.vmall.mapper.TbUserMapper;
import com.vmall.pojo.TbUser;
import com.vmall.pojo.TbUserExample;
import com.vmall.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户处理Service
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${USER_SESSION}")
    private String USER_SESSION;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public VMallResult checkData(String data, int type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        //type == 1表示判断用户名是否可用
        //type == 2表示判断用户手机号是否可用
        //type == 3表示判断用户邮箱是否可用
        if (type == 1) {
            criteria.andUsernameEqualTo(data);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(data);
        } else if (type == 3) {
            criteria.andEmailEqualTo(data);
        } else {
            return VMallResult.build(400, "请求参数中包括非法数据");
        }
        //执行查询
        List<TbUser> list = userMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            //查询到数据，数据不可用，返回false
            return VMallResult.ok(false);
        }
        //数据可用
        return VMallResult.ok(true);
    }

    @Override
    public VMallResult register(TbUser user) {
        //检查数据的有效性
        if (StringUtils.isBlank(user.getUsername())) {
            return VMallResult.build(400, "用户名不能为空");
        }
        //判断用户名是否重复
        VMallResult result = checkData(user.getUsername(), 1);
        if (!(boolean)result.getData()) {
            return VMallResult.build(400,"用户名重复");

        }
        //判断密码是否为空
        if (StringUtils.isBlank(user.getPassword())) {
            return VMallResult.build(400,"密码不能为空");
        }
        //判断电话是否为空
        if (StringUtils.isNotBlank(user.getPhone())) {
            //是否重复校验
            VMallResult result1 = checkData(user.getPhone(), 2);
            if (!(boolean)result1.getData()) {
                return VMallResult.build(400,"电话号码重复");
            }
        }
        //如果email不为空，进行是否重复校验
        if (StringUtils.isNotBlank(user.getEmail())) {
            //是否重复校验
            VMallResult result1 = checkData(user.getEmail(), 3);
            if (!(boolean)result1.getData()) {
                return VMallResult.build(400,"Email重复");
            }
        }
        //补全pojo的属性
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //密码要进行md5加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //输入数据
        userMapper.insert(user);
        //返回注册成功
        return VMallResult.ok();
    }

    @Override
    public VMallResult login(String username, String password) {
        //判断用户名和密码是否正确
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = userMapper.selectByExample(example);
        if (list == null || list.size() == 0) {
            //返回登录失败
            return VMallResult.build(400,"用户名或密码不正确");
        }
        TbUser user = list.get(0);
        //密码要进行md5加密，然后再校验
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            //返回登录失败
            return VMallResult.build(400,"用户名或密码不正确");
        }
        //生成toked，使用uuid
        String token = UUID.randomUUID().toString();
        //清空密码
        user.setPassword(null);
        //把用户信息保存到redis，key就是token，value就是用户信息
        jedisClient.set(USER_SESSION + ":" + token, JsonUtils.objectToJson(user));
        //设置key的过期时间
        jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
        //返回登录成功，其中要把token返回
        return VMallResult.ok(token);
    }

    @Override
    public VMallResult getUserByToken(String token) {
        String json = jedisClient.get(USER_SESSION + ":" + token);
        if (StringUtils.isBlank(json)) {
            return VMallResult.build(400, "用户登录已经过期");
        }
        //重制session的过期时间
        jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
        // 把json转换成user对象
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return VMallResult.ok(user);
//        return VMallResult.ok(json);
    }
}
