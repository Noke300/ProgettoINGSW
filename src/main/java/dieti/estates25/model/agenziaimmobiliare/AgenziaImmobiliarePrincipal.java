package dieti.estates25.model.agenziaimmobiliare;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AgenziaImmobiliarePrincipal implements UserDetails {

    private final AgenziaImmobiliare agenziaimmobiliare;

    public AgenziaImmobiliarePrincipal(AgenziaImmobiliare agenzia) {this.agenziaimmobiliare = agenzia;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public String getPassword() {
        return agenziaimmobiliare.getPasswordAdmin();
    }

    @Override
    public String getUsername() {
        return agenziaimmobiliare.getNomeAgenzia();
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
