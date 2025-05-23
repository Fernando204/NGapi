package com.nginvest.neogenesis.Controller;

import org.springframework.web.bind.annotation.RestController;
import com.nginvest.neogenesis.Model.Investimento;
import com.nginvest.neogenesis.Model.User;
import com.nginvest.neogenesis.Repository.InvestRepository;
import com.nginvest.neogenesis.Repository.UserRepository;
import com.nginvest.neogenesis.DTO.AtualizeInvest;
import com.nginvest.neogenesis.DTO.InvestRequest;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.text.DateFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/invest")
@CrossOrigin("*")
public class investController {
    private InvestRepository investRepository;
    private UserRepository userRepository;

    public investController(InvestRepository investRepository,UserRepository userRepository){
        this.investRepository = investRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/getInvest")//localhost:8080/invest/getInvest?userId=16
    public ResponseEntity<?> getMethodName(@RequestParam long userId) {
        List<Investimento> investimento = investRepository.findByUserId(userId);
        if (investimento.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(investimento);
    }

    @DeleteMapping("/{id}")//para deleter o investimento
    public ResponseEntity<Void> deletInvest(@PathVariable long id){
        if (investRepository.existsById(id)) {
            investRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/update")
    public ResponseEntity<?> setLucro(@RequestBody AtualizeInvest update) {
        Optional<Investimento> invest = investRepository.findById(update.getId());
        if (invest.isPresent()) {
            Investimento investimento = invest.get();
            switch (update.getType()) {
                case "L":
                    
                    break;
                case "U":
                    
                    break;
                case "P":
                    
                    break;
                case "q":
                    String qtd = String.valueOf(update.getValor());
                    investimento.setQuantity(Integer.parseInt(qtd));
                    break;
                default:
                    break;
            }
            investRepository.save(investimento);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("investimento atualizado");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("investimento não encontrado");
        }
    }
    
    @PostMapping("/setInvest")
    public ResponseEntity<?> setInvestment(@RequestBody InvestRequest investRequest){
        long id = investRequest.getUserId();
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usuário não existe");
        }
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String data = time.format(format);
        User user = userOptional.get();
        Investimento investimento = new Investimento(
            investRequest.getType(),
            investRequest.getName(),
            investRequest.getQuantity(), 
            investRequest.getValor(),
            data);

        investimento.setUser(user);
        investRepository.save(investimento);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("investimento salvo");
    }//localhost:8080/invest/setInvest
    
}
