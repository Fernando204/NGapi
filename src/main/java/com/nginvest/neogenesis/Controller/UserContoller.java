package com.nginvest.neogenesis.Controller;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.nginvest.neogenesis.Components.JwtUtil;
import com.nginvest.neogenesis.DTO.LoginRequest;
import com.nginvest.neogenesis.DTO.LoginResponse;
import com.nginvest.neogenesis.Model.User;
import com.nginvest.neogenesis.Repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/user")
public class UserContoller {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public UserContoller(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping("/register")
    public ResponseEntity<?> registerRequest(@RequestBody User user,HttpServletResponse response){
        if (userRepository.existsByEmail(user.getEmail())) {
            System.out.println("Usuario já registrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário já registrado");
        }

        String encriptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encriptedPassword);

        userRepository.save(user);

        String jwt = jwtUtil.generateToken(user);

        Cookie cookie = new Cookie("Jwt_token",jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        response.addCookie(cookie);
        response.setHeader("Set-Cookie", "Jwt_token=" + jwt + "; HttpOnly; Path=/; Max-Age=3600");

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setName(user.getName());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(loginResponse);
        
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500/login.html", allowCredentials= "true")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,HttpServletResponse response){
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não registrado ");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword() )) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha Incorreta");
        }else{

            String jwt = jwtUtil.generateToken(user);

            Cookie cookie = new Cookie("Jwt_token",jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(3600);

            response.addCookie(cookie);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setId(user.getId());
            loginResponse.setName(user.getName());

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(loginResponse);
        }

    }

    @CrossOrigin(origins = "http://127.0.0.1:5500/*", allowCredentials= "true")
    @GetMapping("/session")
    public ResponseEntity<?> getSession(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for(Cookie cookie : cookies){
                if ("Jwt_token".equals(cookie.getName())) {
                    try{
                        long userId = jwtUtil.validateTokenAndGetUserId(cookie.getValue());
                        Optional<User> optionalUser = userRepository.findById(userId);
                        if (optionalUser.isPresent()) {
                            System.out.println("token validado com sucesso");
                            User user = optionalUser.get();

                            LoginResponse loginResponse = new LoginResponse();
                            loginResponse.setId(user.getId());
                            loginResponse.setName(user.getName());

                            return ResponseEntity.ok(loginResponse);
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                        System.out.println("erro ao validar token");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalido");
                    }
                }
            }
        }
        System.out.println("nenhum token encontrado"); 
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não logado, por favor faça login");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> getMethodName(HttpServletResponse response) {
        Cookie cookie = new Cookie("Jwt_token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok("logout realizado com sucesso");
    }
    
}
