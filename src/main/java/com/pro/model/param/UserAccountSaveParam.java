package com.pro.model.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountSaveParam {
    private Long balance;
    private String name;
    private String email;
    private String password;
    private String role;
    private String user_id;
}
