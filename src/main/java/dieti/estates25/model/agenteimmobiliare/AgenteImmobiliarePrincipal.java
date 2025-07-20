package dieti.estates25.model.agenteimmobiliare;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AgenteImmobiliarePrincipal implements UserDetails {

    private final transient AgenteImmobiliare agent;

    public AgenteImmobiliarePrincipal(AgenteImmobiliare agent) {
        this.agent = agent;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("AGENTE_IMMOBILIARE"));
    }

    @Override
    public String getPassword() {
        return agent.getPasswordAgente();
    }

    @Override
    public String getUsername() {
        return agent.getEmailAgente();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
