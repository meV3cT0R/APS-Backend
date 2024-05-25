package com.vector.auto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private UserData userData;
    private String token;
    private String error;

    public LoginResponse(String error) {
        this.error = error;
    }
}
