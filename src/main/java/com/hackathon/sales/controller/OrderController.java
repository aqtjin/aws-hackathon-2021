package com.hackathon.sales.controller;

import com.hackathon.sales.error.BusinessException;
import com.hackathon.sales.error.EmBusinessError;
import com.hackathon.sales.response.CommonReturnType;
import com.hackathon.sales.service.OrderService;
import com.hackathon.sales.service.model.OrderModel;
import com.hackathon.sales.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(originPatterns = "*", allowCredentials="true", allowedHeaders="*")
public class OrderController extends BaseController {

    private final OrderService orderService;

    private final HttpServletRequest httpServletRequest;

    @Autowired
    public OrderController(OrderService orderService, HttpServletRequest httpServletRequest) {
        this.orderService = orderService;
        this.httpServletRequest = httpServletRequest;
    }

    //封装下单请求
    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId")Integer itemId,
                                        @RequestParam(name = "amount")Integer amount,
                                        @RequestParam(name = "promoId", required = false)Integer promoId) throws BusinessException {

        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin == null || !isLogin) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户还未登录");
        }
        //获取用户的登录信息
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, promoId, amount);

        return CommonReturnType.create(null);
    }
}
