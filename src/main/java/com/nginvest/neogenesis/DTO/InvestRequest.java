package com.nginvest.neogenesis.DTO;

public class InvestRequest {
    private String type;
    private String name;
    private int quantity;
    private double valor;
    private long userId;

    public InvestRequest(){}

    public InvestRequest(String type,String name,int quantity,double valor,long userId){
        this.userId = userId; 
        this.type = type;
        this.name = name;
        this.quantity = quantity;
        this.valor = valor;
    }

    public String getType(){return type;}
    public String getName(){return name;}
    public int getQuantity(){return quantity;}
    public double getValor(){return valor;}
    public long getUserId(){return userId;}
}
