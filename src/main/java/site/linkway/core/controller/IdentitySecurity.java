package site.linkway.core.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.val;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.linkway.core.entity.bo.TestRequestBody;
import site.linkway.core.entity.vo.ResultMessage;
import site.linkway.core.entity.vo.StatusResult;
import site.linkway.core.service.IdentitySecurityService;
import site.linkway.core.service.UserDataService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*身份安全验证*/
@Controller
@RequestMapping(value = "/api/identitySecurity")
public class IdentitySecurity {
    /*log4j*/
    static Logger logger = Logger.getLogger(IdentitySecurity.class);
    /*jackson*/
    private ObjectMapper mapper = new ObjectMapper();

    /*对于Autowired idea提示Field injection is not recommended 建议使用set注入*/
    @Autowired
    private IdentitySecurityService identitySecurityService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;
    @Autowired
    HttpSession httpSession;

    /*登录*/
    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String login(@NonNull String id,
                        @NonNull String password,
                        @SessionAttribute(name = "id",required = false) String sessionAttrId
    ) throws JsonProcessingException {
        System.out.println(id+password);
        /*seesion判断 是否存在id 判断是否为登录状态*/
        StatusResult statusResult = new StatusResult();
        if (sessionAttrId!=null&&!sessionAttrId.equals("")) {
            System.out.println(sessionAttrId);
            // 检查是否是管理员
            val userId = userDataService.getUserIdByEmail(sessionAttrId);
            val isAdmin = identitySecurityService.checkIsAdmin(userId);
            statusResult.setAdmin(isAdmin);
            return mapper.writeValueAsString(statusResult);
        }
        //进行登录相关操作
        if (identitySecurityService.checkIdPassword(id, password)) {
            //身份验证成功
            httpSession.setAttribute("id", id);
            // 检查是否是管理员
            val userId = userDataService.getUserIdByEmail(id);
            val isAdmin = identitySecurityService.checkIsAdmin(userId);
            statusResult.setAdmin(isAdmin);
            return mapper.writeValueAsString(statusResult);
        }
        //身份验证失败
        httpServletResponse.setStatus(403);
        return mapper.writeValueAsString(new ResultMessage("用户名或密码错误"));
    }

    /*注册账号*/
    @PostMapping(value = "/register", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String register(@NonNull String password,
                           @NonNull String emailCode,
                           @NonNull @SessionAttribute(name="email") String sessionAttributeEmail,
                           @NonNull @SessionAttribute(name="emailCode") String sessionAttributeEmailCode
    ) throws JsonProcessingException {
        StatusResult statusResult = new StatusResult();
        /*邮箱验证码校验*/
        if (!emailCode.equals(sessionAttributeEmailCode)) {
            statusResult.setResult(false);
            return mapper.writeValueAsString(statusResult);
        }
        //使用邮箱与邮箱验证码进行账号的初步注册
        statusResult.setResult(identitySecurityService.register(sessionAttributeEmail, password));
        /*使验证码失效*/
        httpSession.removeAttribute("emailCode");
        return mapper.writeValueAsString(statusResult);
    }

    /*邮箱验证码身份验证::根据邮箱发送验证码*/
    @PostMapping(value = "/sendEmailCode", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String sendEmailCode(@NonNull String email) throws JsonProcessingException {
        StatusResult statusResult = new StatusResult();
        /*发送验证码 并将邮箱与验证码存进session*/
        String emailCode = identitySecurityService.sendEmailCode(email);
        httpSession.setAttribute("emailCode", emailCode);
        httpSession.setAttribute("email", email);
        return mapper.writeValueAsString(statusResult);
    }

    /*修改密码*/
    @PostMapping(value = "/changePassword", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String changePassword(@NonNull String emailCode,
                                 @NonNull String newPassword,
                                 @NonNull @SessionAttribute(name="email") String sessionAttributeEmail,
                                 @NonNull @SessionAttribute(name="emailCode") String sessionAttributeEmailCode
    ) throws JsonProcessingException {

        StatusResult statusResult = new StatusResult();
        /*邮箱验证码校验*/
        if (!emailCode.equals(sessionAttributeEmailCode)) {
            statusResult.setResult(false);
            return mapper.writeValueAsString(statusResult);
        }
        //使用邮箱与邮箱验证码进行账号的初步注册
        statusResult.setResult(identitySecurityService.changePassword(sessionAttributeEmail, newPassword));
        /*将验证码失效*/
        httpSession.removeAttribute("emailCode");
        return mapper.writeValueAsString(statusResult);
    }


    @PostMapping(value="/testRequestBody",
                produces = "application/json;charset=utf-8")
    @ResponseBody
    /*测试请求体为json类型*/
    public String testRequestBody(@RequestBody TestRequestBody testRequestBody) throws JsonProcessingException {
        System.out.println(testRequestBody);
        return mapper.writeValueAsString(testRequestBody);
    }
}