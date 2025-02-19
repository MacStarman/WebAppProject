package app.Vo;

import lombok.Data;

@Data
public class RegisterVo {
    private String username;
    private String password;
    private String firstname;
    private String surname;
    private String phone;
    private String address;
    private String isAdmin;
}
