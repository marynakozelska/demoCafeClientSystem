package com.example.democafeclientsystem.entities;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String claims;

    public void setClaims(String claims) {
        this.claims = claims;
    }
}
