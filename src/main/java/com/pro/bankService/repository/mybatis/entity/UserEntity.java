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
    private String role;
    private String email;
    private String password;


    public UserEntity(String name) {
        this.name = name;
    }
    public UserEntity(String name, String role) {
        this.name = name;
        this.role = role;
    }
}
