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

import lombok.var;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private PasswordEncoder passwordEncoder;

    
    public CustomAuthenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl){
        this.userDetailsServiceImpl = userDetailsServiceImpl;

    }

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName(); //Ususario ingresado
        var password = authentication.getCredentials().toString(); //Contraseña ingresada

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username); //Buscar usuario

        if(passwordEncoder.matches(password, userDetails.getPassword())){
            //Si todo ha ido correcto, devuelve un token autenticado
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        }else{
             throw new BadCredentialsException("Contraseña incorrecta");
        }

        

        //throw new UnsupportedOperationException("Unimplemented method 'authenticate'");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
