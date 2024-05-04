package com.puccampinas.backendp5noname.services;


import com.puccampinas.backendp5noname.domain.RefreshToken;
import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.dtos.TokenDTO;
import com.puccampinas.backendp5noname.repositories.RefreshTokenRepository;
import com.puccampinas.backendp5noname.repositories.UserRepository;
import com.puccampinas.backendp5noname.services.auth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TokenService tokenService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("username not found") );
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("user id not found")  );
    }

}
