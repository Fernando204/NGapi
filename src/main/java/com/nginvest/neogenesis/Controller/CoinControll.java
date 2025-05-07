package com.nginvest.neogenesis.Controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nginvest.neogenesis.Coins;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/coin")
@CrossOrigin("*")
public class CoinControll{
    ObjectMapper mapa = new ObjectMapper();
    Coins coin = new Coins();

    @GetMapping
    public Map<String,Number> valorMoeda(@RequestParam List<String> base) {
        Map<String,Number> result = new HashMap<>();
        Map<String,Number> map = new HashMap<>();
        for(String s : base){
            switch (s.toLowerCase()) {
                case "btc":
                    map = coin.latestValue(new File("db/bitcoinValue.json")); 
                    break;
                case "usd":
                    map = coin.latestValue(new File("db/USDtoBrl.json"));
                    break;
                case "eur":
                    map = coin.latestValue(new File("db/EURvalues.json"));
                    break;
                case "eth":
                    map = coin.latestValue(new File("db/ethereumValue.json"));
                    break;
                case "sol":
                    map = coin.latestValue(new File("db/solanaValue.json"));
                    break;
                default:
                    continue;
            }
            
            result.putAll(map);
        }

        return result;
    }//localhost:8080/coin?base=btc

    @GetMapping("/test")
    public String getMethodName(@RequestParam String cor) {
        return "<style>body{background: "+cor+"}</style>";
    }
    

    @GetMapping("/min")
    public Map<String,Double> minValue(@RequestParam List<String> base){
        Map<String,Double> response = new HashMap<>();
        Map<String,Double> map = new HashMap<>();

        for(String s : base){
            String[] listaStrings = {"brl","usd","eur"};
            switch (s.toUpperCase()) {
                case "USD":
                    String[] list = {"BRL","EUR"};
                    map = coin.minOrMaxValue(new File("db/USDtoBrl.json"), s,list, 1);
                    break;
                case "EUR":
                    String[] lista = {"BRL","USD"};
                    map = coin.minOrMaxValue(new File("db/EURvalues.json"), s,lista, 1);
                    break;
                case "BTC":
                    map = coin.minOrMaxValue(new File("db/bitcoinValue.json"), s,listaStrings, 1);
                    break;
                case "ETH":
                    map = coin.minOrMaxValue(new File("db/ethereumValue.json"), s, listaStrings, 1);
                    break;
                case "SOL":
                    map = coin.minOrMaxValue(new File("db/solanaValue.json"), s, listaStrings, 1);
                    System.out.println(map);
                    break;
                default:
                    response.put("Default", 0.0);
                    break;
            }
            response.putAll(map);
        }
        return response;
    }

    @GetMapping(value="/max", produces = "application/json")
    public Map<String,Double> maxValue(@RequestParam List<String> base){
        Map<String,Double> response = new HashMap<>();
        Map<String,Double> map = new HashMap<>();

        for(String s : base){
            String[] listaStrings = {"brl","usd","eur"};
            switch (s.toUpperCase()) {
                case "USD":
                    String[] list = {"BRL","EUR"};
                    map = coin.minOrMaxValue(new File("db/USDtoBrl.json"), s,list, 0);
                    break;
                case "EUR":
                    String[] lista = {"BRL","USD"};
                    map = coin.minOrMaxValue(new File("db/EURvalues.json"), s,lista, 0);
                    break;
                case "BTC":
                    map = coin.minOrMaxValue(new File("db/bitcoinValue.json"), s,listaStrings, 0);
                    break;
                case "ETH":
                    map = coin.minOrMaxValue(new File("db/ethereumValue.json"), s, listaStrings, 0);
                    break;
                case "SOL":
                    map = coin.minOrMaxValue(new File("db/solanaValue.json"), s, listaStrings, 0);
                    System.out.println(map);
                    break;
                default:
                    response.put("default", 0.0);
                    break;
            }
            response.putAll(map);
        }
        return response;
    }
   
}
