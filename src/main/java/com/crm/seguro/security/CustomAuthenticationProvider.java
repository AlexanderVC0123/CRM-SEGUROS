package com.crm.seguro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Recuperamos los datos que ha escrito el usuario en el login.
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Buscamos el usuario en la base de datos usando nuestro UserDetailsService.
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            // Si la contrasena coincide, Spring Security considera al usuario autenticado.
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        }

        throw new BadCredentialsException("Contrasena incorrecta");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Indicamos que este provider trabaja con autenticacion usuario/contrasena.
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
