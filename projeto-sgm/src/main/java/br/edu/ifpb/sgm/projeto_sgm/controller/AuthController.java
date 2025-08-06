package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.LoginResquestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.PessoaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.TokenDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.JwtService;
import br.edu.ifpb.sgm.projeto_sgm.service.PessoaService; // Injetando a interface
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PessoaService pessoaService; // Alterado para a interface
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            PessoaService pessoaService) { // Alterado para a interface
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.pessoaService = pessoaService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginResquestDTO login) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getMatricula(), login.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);

        PessoaResponseDTO pessoaDTO = pessoaService.findDtoByMatricula(login.getMatricula());
        TokenDTO tokenDto = new TokenDTO(token, pessoaDTO);

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout realizado com sucesso!");
    }
}