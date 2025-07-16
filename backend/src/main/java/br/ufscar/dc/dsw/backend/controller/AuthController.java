package br.ufscar.dc.dsw.backend.controller;


import br.ufscar.dc.dsw.backend.dto.AuthenticationResponse;
import br.ufscar.dc.dsw.backend.dto.LoginRequest;
import br.ufscar.dc.dsw.backend.dto.RegisterRequest;
import br.ufscar.dc.dsw.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @GetMapping("/teste-seguranca")
    public ResponseEntity<String> testeSeguranca(){
        return ResponseEntity.ok("Se você está lendo isso, seu token é válido!");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(String username, String password, @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(String username, String password, @RequestBody LoginRequest request) {
        AuthenticationResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
