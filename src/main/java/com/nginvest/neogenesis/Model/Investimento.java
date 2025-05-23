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
    private BigDecimal valorUnitario;// valor por cada ação ou cota
    private BigDecimal investimentoInicial;
    private BigDecimal valorTotal;
    private BigDecimal lucro;
    private String data;

    /*Exemplo de Tabela:
    // tipo: ação; nome: ptr4 ; quantidade: 5; valorUnitario: 34,45;investimentoInicial: 172,25; valor total: 172,25; lucro: 0;

     * para renda variavel a quantidade é multiplicada pelo valor unitário 
     * para renda fixa a quantidade é sempre 1 e o valor unitário é igual ao valor total sendo o valor investido pelo usuário
     * para cripto o valor unitário se refere ao valor da crpto na moeda em que foi comprada
     * Exemplo para btc: quantidade: 2BTC ; valorUnitário: 1084584,00 BRL 
     * 
     * a variavel invetimento inicial nunca muda, a não ser que o usuário 
     * altere a quantidade, quando o usuário atualiza um investimento a
     * penas o valor unitário ou a quantidade muda, nesse caso para calcular o lucro subtrai-se
     * do valor total o valor inicial 
     */
    public Investimento(){}

    public Investimento(String type,String name,int quantity,double valorUnitario,String data){ 
        this.type = type;
        this.name = name;
        this.quantity = quantity;
        this.valorUnitario = new BigDecimal(valorUnitario);
        this.valorTotal = new BigDecimal(quantity * valorUnitario);
        this.investimentoInicial = valorTotal;
        this.data = data;
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
    public void setData(String data){
        this.data = data;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
        this.valorTotal = new BigDecimal(quantity * valorUnitario.doubleValue());
        if (quantity < this.quantity) {
            int diferença = this.quantity - quantity;
            investimentoInicial = investimentoInicial.subtract(new BigDecimal(diferença * valorUnitario.doubleValue()));
        }else if(quantity > this.quantity){
            int diferença = quantity - this.quantity;
            investimentoInicial = investimentoInicial.add(new BigDecimal(diferença * valorUnitario.doubleValue()));
        }
    }
    /*
     * q1 = 5
     * vu1 = 10
     * vt = 50
     * iI1 = 50
     * 
     * q2 = 4;
     * dif = 5 - 4 = 1
     * vu2 = 5
     * vt = 20
     * iI2 = iI1 - (dif - vu2) => 50 - (1 * 5) => iI2 = 45
     * L = vt - iI2 => 20 - 45 => L = -25 
     * prejuizo de 25 reais
     */
    public void setValorUnitario(double valorUnitario){
        this.valorUnitario = new BigDecimal(valorUnitario);
        this.valorTotal = new BigDecimal(quantity * valorUnitario);
        this.lucro = valorTotal.subtract(investimentoInicial);
    }
    
    public long getId(){
        return id;
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
    public BigDecimal getValorUnitario(){
        return valorUnitario;
    }
    public BigDecimal getinvestimentoInicial(){
        return investimentoInicial;
    }
    public BigDecimal getValorTotal(){
        return valorTotal;
    }
    public BigDecimal getLucro(){
        return lucro;
    }
    public String getData(){
        return data;
    }

}
