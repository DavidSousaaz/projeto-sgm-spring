package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class RestAppController {

    protected boolean hasPermission(String... roles) {
        Pessoa u = authenticatedUser();
        for (GrantedAuthority ga : u.getAuthorities()) {
            for (String role : roles) {
                if (ga.getAuthority().equalsIgnoreCase(role))
                    return true;
            }
        }
        return false;
    }

    protected boolean isAuthenticationUser(Long id) {
        Pessoa u = authenticatedUser();
        if (id.equals(u.getId()))
            return true;
        return false;
    }

    protected Pessoa authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Pessoa) (authentication.getPrincipal());
    }
}
