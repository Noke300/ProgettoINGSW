package dieti.estates25.config;

import dieti.estates25.service.agenteimmobiliare.AgenteImmobiliareServiceDB;
import dieti.estates25.service.cliente.ClienteServiceDB;
import dieti.estates25.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        String ruolo = null;
        UserDetails userDetails = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {  //la richiesta che arriverà avrà la parola Bearer prima del token
            token = authHeader.substring(7);
            ruolo = jwtService.extractRuolo(token);
            email = jwtService.extractEmail(token);
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if ("cliente".equals(ruolo)) {
                userDetails = context.getBean(ClienteServiceDB.class).loadUserByUsername(email);
            } else if ("agente".equals(ruolo)) {
                userDetails = context.getBean(AgenteImmobiliareServiceDB.class).loadUserByUsername(email);
            } else {
                throw new ServletException("Ruolo inesistente");
            }

            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        if (ruolo != null) {
            String path = request.getRequestURI();

            if (path.startsWith("/auth-cliente") && !"cliente".equals(ruolo)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                response.getWriter().write("Accesso negato: non sei un cliente");
                return;
            }

            if (path.startsWith("/auth-agente") && !"agente".equals(ruolo)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                response.getWriter().write("Accesso negato: non sei un agente");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
