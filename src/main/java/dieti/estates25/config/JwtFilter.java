package dieti.estates25.config;

import dieti.estates25.service.AgenziaImmobiliareServiceDB;
import dieti.estates25.service.AdminSupportServiceDB;
import dieti.estates25.service.AgenteImmobiliareServiceDB;
import dieti.estates25.service.ClienteServiceDB;
import dieti.estates25.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final ApplicationContext context;
    private final TokenBlacklist tokenBlacklist;

    public JwtFilter(JWTService jwtService,
                     ApplicationContext context,
                     TokenBlacklist tokenBlacklist) {
        this.jwtService = jwtService;
        this.context = context;
        this.tokenBlacklist = tokenBlacklist;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        String ruolo = null;
        UserDetails userDetails;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {  //la richiesta che arriverà avrà la parola Bearer prima del token
            token = authHeader.substring(7);
            ruolo = jwtService.extractRuolo(token);
            email = jwtService.extractEmail(token);
        }

        // BLOCCA SE IL TOKEN È NELLA BLACKLIST
        if (tokenBlacklist.isBlacklisted(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalido (blacklisted)");
            return;
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            userDetails = switch (ruolo) {
                case "cliente" -> context.getBean(ClienteServiceDB.class).loadUserByUsername(email);
                case "agente" -> context.getBean(AgenteImmobiliareServiceDB.class).loadUserByUsername(email);
                case "adminSupport" -> context.getBean(AdminSupportServiceDB.class).loadUserByUsername(email);
                case "admin" -> context.getBean(AgenziaImmobiliareServiceDB.class).loadUserByUsername(email);
                default -> throw new ServletException("Ruolo inesistente");
            };

            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, List.of(new SimpleGrantedAuthority(ruolo)));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
