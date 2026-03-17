package com.licenta.crimerate.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String parola;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }
}
