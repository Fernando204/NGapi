package com.nginvest.neogenesis.DTO;

//dto
public class LoginRequest{
    private String email;
    private String password;

    public LoginRequest(){}

    public LoginRequest(String password,String email){
        this.password = password;
        this.email = email;
    }
    public String getPassword(){return password;}
    public String getEmail(){return email;}             
}
