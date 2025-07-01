package dieti.estates25.config;

import dieti.estates25.service.AgenziaImmobiliareServiceDB;
import dieti.estates25.service.adminsupport.AdminSupportServiceDB;
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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity   //serve a dire di usare questa configurazione e non quella di default
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private ClienteServiceDB clientDetailsService;

    @Autowired
    private AgenteImmobiliareServiceDB agentDetailsService;

    @Autowired
    private  AdminSupportServiceDB adminSupportDetailService;

    @Autowired
    private  AgenziaImmobiliareServiceDB agenziaImmobiliareService;

    @Bean
    @Order(1)
    public SecurityFilterChain clienteSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/auth-cliente/**")
                .authenticationProvider(clienteAuthenticationProvider(clientDetailsService))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth-cliente/register", "/auth-cliente/login").permitAll()
                        .requestMatchers("/auth-cliente/**").hasAuthority("cliente")
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
                        .requestMatchers("/auth-agente/**").hasAuthority("agente")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain adminSupportSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/auth-adminSupport/**")
                .authenticationProvider(adminSupportAuthenticationProvider(adminSupportDetailService))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth-adminSupport/register", "/auth-adminSupport/login").permitAll()
                        .requestMatchers("/auth-adminSupport/**").hasAuthority("adminSupport")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain agenziaImmobiliareSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/auth-admin/**")
                .authenticationProvider(agenziaImmobiliareAuthenticationProvider(agenziaImmobiliareService))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth-admin/register", "/auth-admin/login").permitAll()
                        .requestMatchers("/auth-admin/**").hasAuthority("admin")
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

    @Bean
    public AuthenticationProvider adminSupportAuthenticationProvider(AdminSupportServiceDB adminSupportDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(adminSupportDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationProvider agenziaImmobiliareAuthenticationProvider(AgenziaImmobiliareServiceDB agenziaImmobiliareServiceDB) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(agenziaImmobiliareServiceDB);
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

    @Bean(name = "adminSupportAuthenticationManager")
    public AuthenticationManager adminSupportAuthenticationManager(AdminSupportServiceDB adminSupportDetailService) {
        return new ProviderManager(adminSupportAuthenticationProvider(adminSupportDetailService));
    }

    @Bean(name = "agenziaImmobiliareAuthenticationManager")
    public AuthenticationManager agenziaImmobiliareAuthenticationManager(AgenziaImmobiliareServiceDB agenziaImmobiliareServiceDB) {
        return new ProviderManager(agenziaImmobiliareAuthenticationProvider(agenziaImmobiliareServiceDB));
    }

}
