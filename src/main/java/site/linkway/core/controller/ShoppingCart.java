package site.linkway.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.linkway.core.entity.vo.CartList;
import site.linkway.core.entity.vo.StatusResult;
import site.linkway.core.service.ShoppingCartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/user")
public class ShoppingCart {
    static Logger logger= Logger.getLogger(ShoppingCart.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    HttpServletResponse httpServletResponse;
    @Autowired
    HttpSession httpSession;

    /*新增购物车条项*/
    @PostMapping (value = "/cart",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String addCart(@NonNull String goodId,
                          @SessionAttribute(value = "id") String email,
                          @NonNull int num) throws JsonProcessingException {
        StatusResult statusResult=new StatusResult();
        //添加购物车条项
        statusResult.setResult(shoppingCartService.addCart(goodId,email,num));
        return mapper.writeValueAsString(statusResult);
    }

    /*删除购物车条项 同时返回现有条项*/
    @DeleteMapping(value = "/cart",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String addCart(@NonNull String cartId,
                          @SessionAttribute(value = "id") String email
                          ) throws JsonProcessingException {
        CartList cartList=new CartList();
        //删除购物车条项 result为此次操作是否成功
        cartList.result=shoppingCartService.deleteCart(cartId,email);
        //获取此用户的购物车条项
        cartList.setCarts(shoppingCartService.getCartsByEmail(email));
        //结果返回
        return mapper.writeValueAsString(cartList);
    }

    /*获取购物车全部条项*/
    @GetMapping(value = "/cart",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String addCart(@SessionAttribute(value = "id") String email) throws JsonProcessingException {
        CartList cartList=new CartList();
        //获取购物车条项
        cartList.setCarts(shoppingCartService.getCartsByEmail(email));
        return mapper.writeValueAsString(cartList);
    }

}
