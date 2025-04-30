package dieti.estates25.controllers;

import dieti.estates25.config.TokenBlacklist;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.blacklist(token);
            return ResponseEntity.ok("Logout effettuato con successo");
        }

        return ResponseEntity
                .badRequest()
                .body("Token non presente o non valido");
    }

}
