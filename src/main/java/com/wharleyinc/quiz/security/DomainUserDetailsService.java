package com.wharleyinc.quiz.security;

import com.wharleyinc.quiz.domain.User;
import com.wharleyinc.quiz.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String userName) {
        log.debug("Authenticating {}", userName);

        if (new EmailValidator().isValid(userName, null)) {
            return userRepository
                    .findOneWithAuthoritiesByEmailIgnoreCase(userName)
                    .map(user -> createSpringSecurityUser(userName, user))
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + userName + " was not found in the database"));
        }

        String lowercaseUserName = userName.toLowerCase(Locale.ENGLISH);
        return userRepository
                .findOneWithAuthoritiesByUserName(lowercaseUserName)
                .map(user -> createSpringSecurityUser(lowercaseUserName, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseUserName + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseUserName, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseUserName + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user
                .getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), grantedAuthorities);
    }
}
