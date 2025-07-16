package br.ufscar.dc.dsw.backend.service;

import br.ufscar.dc.dsw.backend.domain.User;
import br.ufscar.dc.dsw.backend.dto.AuthenticationResponse;
import br.ufscar.dc.dsw.backend.dto.LoginRequest;
import br.ufscar.dc.dsw.backend.dto.RegisterRequest;
import br.ufscar.dc.dsw.backend.repository.UserRepository;
import br.ufscar.dc.dsw.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {

        Optional<User> user = userRepository.findByUsername(request.username());
        if(user.isPresent()){
            throw new RuntimeException("Este nome de usuário já está em uso.");
        }

        User novoUser = new User();
        novoUser.setUsername(request.username());
        novoUser.setEmail(request.email());
        novoUser.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(novoUser);
    }

    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()));

        var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado após autenticação"));

        var jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken, user.getId(), user.getUsername());

    }

}
