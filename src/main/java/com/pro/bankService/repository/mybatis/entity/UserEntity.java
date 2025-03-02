package com.pro.bankService.repository.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private String user_id;
    private String name;
    private String email;
    private String password;
    private String role;

    public UserEntity(String name) {
        this.name = name;
    }
}
