package com.arete.korbly.infrastructure.security;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.exceptions.InvestorNotFound;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByPrimaryContactEmail((username));
        if (appUser.isPresent()){
            return new UserPrincipal(getAuthorities(appUser.get()), appUser.get());
        }
        throw new InvestorNotFound();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(AppUser user){
        String userRole = "ROLE_" + user.getUserType();
        return List.of(new SimpleGrantedAuthority(userRole));
    }

}
