package com.pro.securityAuth;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class RefreshEntity {

    private Long id;
    private String email;
    private String refresh;
    private String expiration;

    public RefreshEntity(String email, String refresh, String expiration) {
        this.email = email;
        this.refresh = refresh;
        this.expiration = expiration;
    }


}
