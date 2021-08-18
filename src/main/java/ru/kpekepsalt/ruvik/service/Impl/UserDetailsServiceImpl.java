package ru.kpekepsalt.ruvik.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kpekepsalt.ruvik.model.AppUserDetails;
import ru.kpekepsalt.ruvik.model.User;
import ru.kpekepsalt.ruvik.repository.UserRepository;

import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(s).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found")));
        Set<SimpleGrantedAuthority> authorities = user.getRole().getAuthorities();
        AppUserDetails appUserDetails = new AppUserDetails(user);
        return appUserDetails;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean hasAuthority(String authority) {
        return  getAuthentication().getAuthorities().stream()
                .anyMatch(
                        grantedAuthority -> grantedAuthority.getAuthority()
                                .equals(authority)
                );
    }

    public AppUserDetails getUserDetails() {
        AppUserDetails user = (AppUserDetails) getAuthentication().getPrincipal();
        return user;
    }

    public Long getUserid() {
        return getUserDetails().getUser().getId();
    }

    public User getUser() {
        return getUserDetails().getUser();
    }

    public boolean isAuthenticated() {
        return getAuthentication().isAuthenticated();
    }
}
