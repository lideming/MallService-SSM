package site.linkway.core.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//购物车条项
public class Cart {
    public String cartId;
    public String goodId;
    public String userId;
    public int num;
}
