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


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/alunos").permitAll()

                        .requestMatchers(HttpMethod.PUT, "/api/monitorias/{monitoriaId}/inscricoes/{alunoId}/selecionar").hasAnyRole(ADMIN, COORDENADOR)
                        .requestMatchers(HttpMethod.POST, "/api/monitorias/{id}/inscricoes").hasRole(DISCENTE)
                        .requestMatchers(HttpMethod.GET, "/api/monitorias/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE, DISCENTE)
                        .requestMatchers("/api/monitorias/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE)

                        .requestMatchers(HttpMethod.GET, "/api/alunos/{id}").hasAnyRole(ADMIN, COORDENADOR, DOCENTE, DISCENTE)
                        .requestMatchers(HttpMethod.GET, "/api/alunos/me/inscricoes").hasRole(DISCENTE)
                        .requestMatchers(HttpMethod.PUT, "/api/alunos/{id}").hasAnyRole(ADMIN, COORDENADOR, DISCENTE)
                        .requestMatchers("/api/alunos/**").hasAnyRole(ADMIN, COORDENADOR)

                        .requestMatchers("/api/pessoas/**").hasAnyRole(ADMIN, COORDENADOR)

                        .requestMatchers("/api/instituicoes/**").hasRole(ADMIN)
                        .requestMatchers("/api/cursos/**").hasAnyRole(ADMIN, COORDENADOR)
                        .requestMatchers(HttpMethod.GET, "/api/disciplinas/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE)
                        .requestMatchers("/api/disciplinas/**").hasAnyRole(ADMIN, COORDENADOR)

                        .requestMatchers(HttpMethod.GET, "/api/processos-seletivos/**").authenticated()
                        .requestMatchers("/api/processos-seletivos/**").hasAnyRole(ADMIN, COORDENADOR)

                        .requestMatchers(HttpMethod.PATCH, "/api/atividades/{id}/status").hasAnyRole(ADMIN, COORDENADOR, DOCENTE)
                        .requestMatchers(HttpMethod.GET, "/api/atividades/**").authenticated()
                        .requestMatchers("/api/atividades/**").hasAnyRole(ADMIN, COORDENADOR, DOCENTE, DISCENTE)
                        .requestMatchers(HttpMethod.GET, "/api/professores/**").authenticated()
                        .requestMatchers("/api/professores/**").hasAnyRole(ADMIN, COORDENADOR)

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}