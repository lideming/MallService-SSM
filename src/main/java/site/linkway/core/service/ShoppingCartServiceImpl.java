package site.linkway.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.linkway.core.controller.ImageDistribution;
import site.linkway.core.entity.po.Cart;
import site.linkway.core.entity.po.GoodImg;
import site.linkway.core.entity.po.User;
import site.linkway.core.entity.po.CartItem;
import site.linkway.core.mapper.CartMapper;
import site.linkway.core.mapper.GoodImgMapper;
import site.linkway.core.mapper.GoodMapper;
import site.linkway.core.mapper.UserMapper;
import site.linkway.utils.UUIDUtils;


import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
    @Autowired
    CartMapper cartMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    GoodImgMapper goodImgMapper;

    @Override
    public boolean addCart(String goodId, String email, int num) {
        String userId=userMapper.selectIdByEmail(email);
        Cart inertCart=new Cart(UUIDUtils.getUUID(),goodId,userId,num);
        return 1==cartMapper.insert(inertCart);
    }

    @Override
    public boolean deleteCart(String cartId, String email) {
        //根据邮箱寻找用户
        User user=new User();user.setEmail(email);
        User targetUser=userMapper.select(user);
        Cart delCart=new Cart();
        delCart.setCartId(cartId);
        delCart.setUserId(targetUser.getUserId());
        return 1==cartMapper.delete(delCart);
    }


    @Override
    public List<CartItem> getCartsByEmail(String email) {
        //根据邮箱寻找用户
        User user=new User();user.setEmail(email);
        User targetUser=userMapper.select(user);
        Cart cart=new Cart();
        cart.setUserId(targetUser.getUserId());
        /*根据购物车条项检索出物品详情*/
        List<CartItem> cartItemList=cartMapper.selectCartsDetail(email);
        /*需要根据每个商品将其相关图片检索出来*/
        for(CartItem item:cartItemList){
            //根据goodId检索出imgId
            List<String> goodIds=goodImgMapper.selectImgIdByGoodId(item.getGoodId());
            for(int i=0;i<goodIds.size();i++){
                String URL=goodIds.get(i);
                URL=ImageDistribution.formatURLFromImgId(URL);
                goodIds.set(i,URL);
            }
            item.setImgsURL(goodIds);
        }
        return cartItemList;
    }



}
