package mediHub_be.security.securitycustom;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private final long userSeq;
    private final String userId;
    private final String userName;
    private final String userPassword;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(long userSeq, String userId, String userName, String userPassword, Collection<? extends GrantedAuthority> authorities) {
        this.userSeq = userSeq;
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
