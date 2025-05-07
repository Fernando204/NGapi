package com.nginvest.neogenesis.DTO;

public class AtualizeInvest {
    private long id;
    private String type;
    private double valor;

    public AtualizeInvest(long id,double valor,String type){
        this.id = id;
        this.valor = valor;
        this.type = type;
    }

    public String getType(){return type;}
    public long getId(){return id;}
    public double getValor(){return valor;}
}
