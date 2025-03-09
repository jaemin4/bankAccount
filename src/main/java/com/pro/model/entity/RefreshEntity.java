package com.pro.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class RefreshEntity {

    private Long id;
    private String email;
    private String refresh_token;
    private String refresh_expiration;
    private String access_token;
    private String access_expiration;

    public RefreshEntity(String email, String refresh_token, String refresh_expiration, String access_token, String access_expiration) {
        this.email = email;
        this.refresh_token = refresh_token;
        this.refresh_expiration = refresh_expiration;
        this.access_token = access_token;
        this.access_expiration = access_expiration;
    }



}
