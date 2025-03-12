package beans;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserService {

    private String name;

    private UserDao userDao;

    public UserService() {}

    public UserService(String name) {
        this.name = name;
    }
    
    public void queryUserInfo(){
        System.out.println("查询用户信息");
    }
    
}
