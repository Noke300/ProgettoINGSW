package dieti.estates25.config;

import dieti.estates25.service.agenteimmobiliare.AgenteImmobiliareService;
import dieti.estates25.service.agenteimmobiliare.AgenteImmobiliareServiceDB;
import dieti.estates25.service.cliente.ClienteServiceDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity   //serve a dire di usare questa configurazione e non quella di default
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private ClienteServiceDB clientDetailsService;

    @Autowired
    private AgenteImmobiliareServiceDB agentDetailsService;

    @Bean
    @Order(1)
    public SecurityFilterChain clienteSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/auth-cliente/**")
                .authenticationProvider(clienteAuthenticationProvider(clientDetailsService))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth-cliente/register", "/auth-cliente/login").permitAll()
                        //.requestMatchers("/auth-agente/**").denyAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain agentSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/auth-agente/**")
                .authenticationProvider(agenteAuthenticationProvider(agentDetailsService))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth-agente/register", "/auth-agente/login").permitAll()
                        //.requestMatchers("/auth-cliente/**").denyAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider clienteAuthenticationProvider(ClienteServiceDB clientDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(clientDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationProvider agenteAuthenticationProvider(AgenteImmobiliareServiceDB agentDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(agentDetailsService);
        return provider;
    }

    @Primary
    @Bean(name = "clientAuthenticationManager")
    public AuthenticationManager clientAuthenticationManager(ClienteServiceDB clientDetailsService) {
        return new ProviderManager(clienteAuthenticationProvider(clientDetailsService));
    }

    @Bean(name = "agentAuthenticationManager")
    public AuthenticationManager agentAuthenticationManager(AgenteImmobiliareServiceDB agentDetailsService) {
        return new ProviderManager(agenteAuthenticationProvider(agentDetailsService));
    }

}
