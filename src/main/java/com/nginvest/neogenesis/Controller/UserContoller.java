package com.nginvest.neogenesis.Controller;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.nginvest.neogenesis.DTO.LoginRequest;
import com.nginvest.neogenesis.Model.User;
import com.nginvest.neogenesis.Repository.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserContoller {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserContoller(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerRequest(@RequestBody User user){
        if (userRepository.existsByEmail(user.getEmail())) {
            System.out.println("Usuario já registrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário já registrado");
        }

        String encriptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encriptedPassword);

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
        
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não registrado ");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword() )) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha Incorreta");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);

    }
    
    
}
