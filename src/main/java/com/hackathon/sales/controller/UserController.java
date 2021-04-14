package com.hackathon.sales.controller;

import com.hackathon.sales.controller.viewobject.UserVO;
import com.hackathon.sales.error.BusinessException;
import com.hackathon.sales.error.EmBusinessError;
import com.hackathon.sales.response.CommonReturnType;
import com.hackathon.sales.service.UserService;
import com.hackathon.sales.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",originPatterns = "*")
//@CrossOrigin(allowCredentials = "true", origins = "http://localhost:63342" )
public class UserController extends BaseController {

    @Autowired
    private  UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;

//    @Autowired
//    public UserController(UserService userService, HttpServletRequest httpServletRequest) {
//        this.userService = userService;
//        this.httpServletRequest = httpServletRequest;
//    }

    //用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody

    //@CrossOrigin(originPatterns = "*", allowCredentials="true", allowedHeaders="*")
    public CommonReturnType register(@RequestParam(name="telephone")String telephone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpCode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        boolean flag = false;
        if (otpCode == null) flag = (inSessionOtpCode == null);
        else flag = otpCode.equals(inSessionOtpCode);
        if (!flag) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EnCodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }
    public String EnCodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        // 加密字符串
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }

    //用户获取otp短信接口
    @RequestMapping(value="/getotp", method={RequestMethod.POST}, consumes={CONTENT_TYPE_FORMED})
    @ResponseBody

    //@CrossOrigin(originPatterns = "*", allowCredentials="true", allowedHeaders="*")
    public CommonReturnType getOtp(@RequestParam(name="telephone") String telephone) {
        //按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //将OTP验证码同对应用户的手机号关联, 使用httpsession的方式绑定手机号和OTP验证码
        httpServletRequest.getSession().setAttribute(telephone, otpCode);

        //将OTP验证码通过短信通道发送给用户（省略）
        System.out.println("telephone = " + telephone + "& otpCode = " + otpCode);

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        UserVO userVO = convertFromModel(userModel);

        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) return null;

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

}
