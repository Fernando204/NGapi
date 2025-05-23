package com.nginvest.neogenesis;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
//18167-122
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public  class Coins {
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper mapper = new ObjectMapper();

    private String btcFile = "db/bitcoinValue.json";
    private String usdFile =  "db/EURvalues.json";
    private String eurFile = "db/USDtoBrl.json";
    private String ethFile = "db/ethereumValue.json";
    private String solFile = "db/solanaValue.json";

    private void createDBdir(){
        File pasta = new File("db");

        if (!pasta.exists()) {
            boolean criada = pasta.mkdir();

            if (criada) {
                System.out.println("pasta criada com sucesso");
            }else{
                System.out.println("erro ao criar pasta");
            }
        }else{
            return;
        }
    }

    public Map<String,Double> minOrMaxValue(File file,String base,String[] list,int tipo){
        try{
            Map<String,Object> response = mapper.readValue(file, HashMap.class);
            Map<String,Double> resposta = new HashMap<>();

            Map<String,Map<String,Double>> moedas = new HashMap<>();

            for(String moeda : list){
                moedas.put(moeda, new HashMap<>());
            }

            for(Map.Entry<String,Object> entry : response.entrySet()){
                Map<String,Double> values = (Map<String,Double>) entry.getValue();

                for(String moeda : list){
                    base = base.toUpperCase();
                    if (base.equals("BTC")) moeda = moeda.toLowerCase(); 
                    String chave = moeda+"to"+base;
                
                    Object val = values.get(chave);
                    if (val instanceof Number) {
                        moedas.get(moeda).put(entry.getKey(), ((Number) val).doubleValue());
                    }
                }
            }
            for(String moeda : list){    
                double max = 0;
                if (tipo == 0) {
                    max = Collections.max(moedas.get(moeda).values());
                }else if (tipo == 1) {
                    max = Collections.min(moedas.get(moeda).values());
                }
                resposta.put(moeda+"to"+base, max);
           }

            return resposta;
        }catch(Exception ex){
            ex.printStackTrace();
            return new HashMap<>();
        }
    }

    public Map<String,Number> latestValue(File file){
        try{
            Map<String,Object> response = mapper.readValue(file, HashMap.class);
            String latestTime = Collections.max(response.keySet());
            System.out.println(latestTime);

            return (Map<String,Number>) response.get(latestTime);
        }catch(Exception ex){
            ex.printStackTrace();
            return new HashMap<>();
        }
    }
    public Map<String,Number> oldestValue(File file){
        try{
            Map<String,Object> response = mapper.readValue(file, HashMap.class);
            String latestTime = Collections.min(response.keySet());
            System.out.println(latestTime);

            return (Map<String,Number>) response.get(latestTime);
        }catch(Exception ex){
            ex.printStackTrace();
            return new HashMap<>();
        }
    }
    private void saveBTCprice(String cripto,String sigla,RestTemplate restTemplate,String url){

        try{
            Map<String,Object> db = new HashMap<>();
            Map<String,Object> response = restTemplate.getForObject(url, Map.class);
            Map<String,Number> rates = (Map<String,Number>) response.get(cripto);
            
            File file = new File("db/"+cripto+"Value.json");

            if (file.exists()) {
                db = mapper.readValue(file, HashMap.class);
            }
            
            LocalDateTime time = LocalDateTime.now();//retorna a data atual
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("HH:mm");//cria uma formatação de Horario
            String data = time.format(formater);//formata o horario 

            Map<String,Number> values = new HashMap<>();
            for (Map.Entry<String,Number> entry : rates.entrySet()) {
                values.put(entry.getKey()+"to"+sigla, entry.getValue());
            }
            db.put(data, values);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, db);
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("erro ao pegar valor do bitcoin");
        }
    }
    private void saveUsd(RestTemplate restTemplate, String url,String url2){

        try{
            Map<String,Object> response =(Map<String,Object>) restTemplate.getForObject(url, HashMap.class);
            Map<String,Object> response2 = restTemplate.getForObject(url2, HashMap.class);
            Map<String,Object> db = new HashMap<>();

            File file = new File("db/USDtoBrl.json");
            if (file.exists()) {
                db = mapper.readValue(file, HashMap.class);
            }

            LocalDateTime time = LocalDateTime.now();//retorna a data atual
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("HH:mm");//cria uma formatação de Horario
            String data = time.format(formater);//formata o horario 

            Map<String,Object> values = new HashMap<>();
            Map<String,Number> usdValue = (Map<String,Number>) response.get("usd");
      
            values.put("BRLtoUSD",usdValue.get("brl"));
            usdValue = (Map<String,Number>) response2.get("rates");
            values.put("EURtoUSD", usdValue.get("EUR"));

            db.put(data, values);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, db);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private void saveEURvalue(RestTemplate restTemplate,String url){
        Map<String,Object> db = new HashMap<>();
        File file = new File("db/EURvalues.json");
        try{
            if (file.exists()) {
                db = mapper.readValue(file, HashMap.class);
            }

            Map<String,Object> response = restTemplate.getForObject(url, HashMap.class);
            Map<String,Number> rates = (Map<String,Number>) response.get("rates");
            Map<String,Number> values = new HashMap<>();

            for(Map.Entry<String,Number> entry : rates.entrySet()){
                values.put(entry.getKey()+"toEUR", entry.getValue());
            }

            LocalDateTime time = LocalDateTime.now();//retorna a data atual
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("HH:mm");
            String data = time.format(formater);//formata o dia

            db.put(data, values);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, db);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void saveArchive(String file,Map<?,?> map){
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(file), map);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
   
    @Scheduled(cron = "3 */2 * * * *")
    public void getCoinsPrice(){
        System.out.println("coletando dados das Moedas...");
        createDBdir();

        String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd,brl,eur";
        String url4 = "https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=usd,brl,eur";
        String url5 = "https://api.coingecko.com/api/v3/simple/price?ids=solana&vs_currencies=usd,brl,eur";


        String url1 = "https://api.coingecko.com/api/v3/simple/price?ids=usd&vs_currencies=brl";
        String url2 = "https://api.frankfurter.app/latest?from=EUR&to=BRL,USD";
        String url3 = "https://api.frankfurter.app/latest?from=USD&to=EUR";
        
        saveBTCprice("bitcoin","BTC",restTemplate, url);
        saveBTCprice("ethereum", "ETH", restTemplate, url4);
        saveBTCprice("solana", "SOL", restTemplate, url5);

        saveUsd(restTemplate, url1,url3);
        saveEURvalue(restTemplate, url2);
        System.out.println("Dados Salvos");
    }

    @Scheduled(cron = "1 35 15 * * *")
    public void clearCoinsPrice(){
        try{
            LocalDateTime time = LocalDateTime.now();//retorna a data atual
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM");
            String data = time.format(formater);//formata o dia

            Map<String,Object> lastValue = new HashMap<>();

            lastValue.put(data, latestValue(new File(btcFile)));
            saveArchive("db/LastBTCPrice.json", lastValue);

            lastValue.clear();
            lastValue.put(data, latestValue(new File(usdFile)));
            saveArchive("db/lastUSDprice.json", lastValue);

            lastValue.clear();
            lastValue.put(data, latestValue(new File(eurFile)));
            saveArchive("db/lastEURprice.json", lastValue);

            lastValue.clear();
            lastValue.put(data, latestValue(new File(ethFile)));
            saveArchive("db/lastETHprice.json", lastValue);

            lastValue.clear();
            lastValue.put(data, latestValue(new File(solFile)));
            saveArchive("db/lastSOLprice.json", lastValue);//moeda solana
            
            saveArchive(btcFile, new HashMap<>());
            saveArchive(eurFile, new HashMap<>());
            saveArchive(usdFile, new HashMap<>());
            saveArchive(ethFile, new HashMap<>());
            saveArchive(solFile, new HashMap<>());

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
