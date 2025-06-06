package com.nginvest.neogenesis.Model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<Investimento> investimento;

    public User(){}

    public User(String name,String email,String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getName(){return name;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public long getId(){return id;}
}
