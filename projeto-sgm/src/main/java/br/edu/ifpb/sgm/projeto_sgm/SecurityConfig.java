package br.edu.ifpb.sgm.projeto_sgm;

import br.edu.ifpb.sgm.projeto_sgm.util.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static br.edu.ifpb.sgm.projeto_sgm.util.Constants.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Bean de configuração do CORS centralizado
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // URL do seu front-end React
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // --- INÍCIO DO BLOCO CORRIGIDO ---
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/alunos").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/monitorias/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE, DISCENTE)
                        .requestMatchers("/api/monitorias/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE)

                        .requestMatchers(HttpMethod.PUT, "/api/alunos/{id}").hasAnyRole(ADMIN, COORDENADOR, DISCENTE)
                        .requestMatchers("/api/alunos/**").hasAnyRole(ADMIN, COORDENADOR)

                        .requestMatchers("/api/pessoas/**").hasAnyRole(ADMIN, COORDENADOR) // Apenas admin/coord podem ver todas as pessoas

                        // Regras que já estavam corretas
                        .requestMatchers("/api/instituicoes/**").hasRole(ADMIN)
                        .requestMatchers("/api/cursos/**").hasAnyRole(ADMIN, COORDENADOR)
                        .requestMatchers(HttpMethod.GET, "/api/disciplinas/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE)
                        .requestMatchers("/api/disciplinas/**").hasAnyRole(ADMIN, COORDENADOR)
                        .requestMatchers("/api/processos-seletivos/**").hasAnyRole(ADMIN, COORDENADOR)
                        .requestMatchers(HttpMethod.GET, "/api/atividades/**").authenticated()
                        .requestMatchers("/api/atividades/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE, DISCENTE)
                        .requestMatchers(HttpMethod.GET, "/api/professores/**").authenticated()
                        .requestMatchers("/api/professores/**").hasAnyRole(ADMIN, COORDENADOR)

                        .anyRequest().authenticated()
                )
                // --- FIM DO BLOCO CORRIGIDO ---
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}