package com.nginvest.neogenesis.Model;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "investimentos")
public class Investimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;
    private String name;
    private int quantity;
    private double valor;
    private BigDecimal valorTotal;
    private double lucro;
    private double prejuizo;

    public Investimento(){}

    public Investimento(String type,String name,int quantity,double valor){ 
        this.type = type;
        this.name = name;
        this.quantity = quantity;
        this.valor = valor;
        this.valorTotal = valorTotal.add(new BigDecimal(quantity * valor));
    }

    @ManyToOne
    @JoinColumn(name = "users_id",nullable = false)
    private User user;

    public void setUser(User user){
        this.user = user;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
        this.valorTotal = valorTotal.add(new BigDecimal(quantity * valor));
        this.valorTotal = valorTotal.add(new BigDecimal(lucro));
        this.valorTotal = valorTotal.subtract(new BigDecimal(prejuizo));

    }
    public void setValor(double valor){
        this.valor = valor;
        this.valorTotal = valorTotal.add(new BigDecimal(quantity * valor));
        this.valorTotal = valorTotal.add(new BigDecimal(lucro));
        this.valorTotal = valorTotal.subtract(new BigDecimal(prejuizo));
    }
    public void setLucro(double lucro){
        this.lucro = lucro;
        this.valorTotal = valorTotal.add(new BigDecimal(quantity * valor));
        this.valorTotal = valorTotal.add(new BigDecimal(lucro));
        this.valorTotal = valorTotal.subtract(new BigDecimal(prejuizo));
    }
    public void setPrejuizo(double prejuizo){
        this.prejuizo= prejuizo;
        this.valorTotal = valorTotal.add(new BigDecimal(quantity * valor));
        this.valorTotal = valorTotal.add(new BigDecimal(lucro));
        this.valorTotal = valorTotal.subtract(new BigDecimal(prejuizo));
    }


    public String getType(){
        return type;
    }
    public String getName(){
        return name;
    }
    public int getQuantity(){
        return quantity;
    }
    public double getValor(){
        return valor;
    }
    public BigDecimal getValorTotal(){
        return valorTotal;
    }
    public double getLucro(){
        return lucro;
    }
    public double getPrejuizo(){
        return prejuizo;
    }

}
