package site.linkway.core.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//用户收货地址
public class Address {
    public String addressId;
    public String userId;
    public String phone;
    public String address;
    public String name;
}
