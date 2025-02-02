package site.linkway.core.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*用户个人信息*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalData {
    public String userId;
    public String name;
    public String sex;
    public String profilePhotoURL;
    public double money;
    public String email;
}
