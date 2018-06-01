package com.vmall.sso.controller;

import com.vmall.common.pojo.VMallResult;
import com.vmall.common.utils.CookieUtils;
import com.vmall.common.utils.JsonUtils;
import com.vmall.pojo.TbUser;
import com.vmall.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户处理Controller
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public VMallResult checkUserData(@PathVariable String param, @PathVariable Integer type) {
        VMallResult result = userService.checkData(param, type);
        return result;
    }

    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public VMallResult register(TbUser user) {
        VMallResult result = userService.register(user);
        return result;
    }

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public VMallResult login(String username, String password,
                             HttpServletResponse response, HttpServletRequest request) {
        VMallResult result = userService.login(username, password);
        //如果token不为null则写如cookie，如果直接写入在没有token的时候会包空指针异常
        //如果登录成功才把token写入cookie
        if (result.getStatus() == 200) {
            //把token写入cookie
            CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
        }
        return result;
    }

    /*//produces可以指定响应数据的content-type
    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback) {
        VMallResult result = userService.getUserByToken(token);
        //判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }*/
    //jsonp的第二种方法，仅限spring4.1以上版本
    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback) {
        VMallResult result = userService.getUserByToken(token);
        //判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            //设置回调方法
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }
}
