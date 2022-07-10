package dev.drawethree.LicenseSystem.security.service;

import dev.drawethree.LicenseSystem.auth.user.UserDetailsImpl;
import dev.drawethree.LicenseSystem.user.model.User;
import dev.drawethree.LicenseSystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Service
@Transactional
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    public SecurityServiceImpl(UserRepository userRepository, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, @Qualifier("customAuthenticationManager") AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @Override
    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    @Override
    public User getCurrentUser() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = userRepository.findById(userDetails.getUserId());

            if (optionalUser.isEmpty()) {
                throw new UserPrincipalNotFoundException(userDetails.getUsername());
            }

            return optionalUser.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}