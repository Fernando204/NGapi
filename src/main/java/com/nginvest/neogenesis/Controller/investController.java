package com.nginvest.neogenesis.Controller;

import org.springframework.web.bind.annotation.RestController;
import com.nginvest.neogenesis.Model.Investimento;
import com.nginvest.neogenesis.Model.User;
import com.nginvest.neogenesis.Repository.InvestRepository;
import com.nginvest.neogenesis.Repository.UserRepository;
import com.nginvest.neogenesis.DTO.AtualizeInvest;
import com.nginvest.neogenesis.DTO.InvestRequest;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/invest")
public class investController {
    private InvestRepository investRepository;
    private UserRepository userRepository;

    public investController(InvestRepository investRepository,UserRepository userRepository){
        this.investRepository = investRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/getInvest")
    public ResponseEntity<?> getMethodName(@RequestParam long userId) {
        List<Investimento> investimento = investRepository.findByUserId(userId);
        if (investimento.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(investimento);
    }
    
    @PostMapping("/update")
    public ResponseEntity<?> setLucro(@RequestBody AtualizeInvest update) {
        Optional<Investimento> invest = investRepository.findById(update.getId());
        if (invest.isPresent()) {
            Investimento investimento = invest.get();
            switch (update.getType()) {
                case "L":
                    investimento.setLucro(update.getValor());
                    break;
                case "U":
                    investimento.setValor(update.getValor());
                    break;
                case "P":
                    investimento.setPrejuizo(update.getValor());
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
        Investimento investimento = new Investimento();
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usuário não existe");
        }
        User user = userOptional.get();

        investimento.setType(investRequest.getType());
        investimento.setName(investRequest.getName());
        investimento.setQuantity(investRequest.getQuantity());
        investimento.setValor(investRequest.getValor());
        investimento.setUser(user);
        
        investRepository.save(investimento);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("investimento salvo");
    }
    
}
