package com.nginvest.neogenesis.DTO;

public class LoginResponse {
    private String name;
    private long id;
    
    public LoginResponse(){}

    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
