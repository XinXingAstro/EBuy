package com.vmall.order.interceptor;

import com.vmall.common.pojo.VMallResult;
import com.vmall.common.utils.CookieUtils;
import com.vmall.common.utils.JsonUtils;
import com.vmall.pojo.TbUser;
import com.vmall.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 判断用户是否登录的拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    @Value("${SSO_URL}")
    private String SSO_URL;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //执行handler之前，先执行此方法
        //判断用户是否登录业务逻辑：
        //1、从cookie中取token信息
        String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
        //2、如果取不到token，跳转到sso的登录页面，需要把当前请求的url作为参数传递给sso，sso登录成功后跳转会当前页面。
        if (StringUtils.isBlank(token)) {
            //取当前请求页面的url
            String requestURL = request.getRequestURL().toString();
            //跳转到登录页面
            response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
            //拦截
            return false;
        }
        //3、如果取到token，调用sso系统服务判断用户是否登录
        VMallResult result = userService.getUserByToken(token);
        //4、如果用户未登录，既没取到用户信息，跳转到sso的登录页面
        if (result.getStatus() != 200) {
            //取当前请求页面的url
            String requestURL = request.getRequestURL().toString();
            //跳转到登录页面
            response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
            //拦截
            return false;
        }
        //5、如果取到用户信息，放行。
        //把用户信息放到request中
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user", user);
        //返回值如果返回true：放行，返回false：拦截
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //在handler执行之后，modelAndView返回之前，执行此方法，可以对modelAndView进行一些处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //modelAndView返回之后执行此方法，异常处理。
    }
}
