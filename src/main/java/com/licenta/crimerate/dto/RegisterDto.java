package com.licenta.crimerate.dto;

public class RegisterDto {
    private String numeComplet;
    private String email;
    private String parola;

    // Getteri și Setteri
    public String getNumeComplet() { return numeComplet; }
    public void setNumeComplet(String numeComplet) { this.numeComplet = numeComplet; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getParola() { return parola; }
    public void setParola(String parola) { this.parola = parola; }
}