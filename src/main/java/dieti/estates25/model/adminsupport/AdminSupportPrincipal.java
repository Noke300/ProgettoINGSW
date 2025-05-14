package dieti.estates25.model.adminsupport;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AdminSupportPrincipal implements UserDetails {

    private final AdminSupport adminSupport;
    public AdminSupportPrincipal(AdminSupport adminSupport) {
        this.adminSupport = adminSupport;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ADMIN_SUPPORT"));
    }

    @Override
    public String getPassword() {
        return adminSupport.getPasswordSupport();
    }

    @Override
    public String getUsername() {
        return adminSupport.getEmailSupport();
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
