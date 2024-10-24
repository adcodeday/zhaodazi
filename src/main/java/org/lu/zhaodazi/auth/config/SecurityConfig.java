package org.lu.zhaodazi.auth.config;

import org.lu.zhaodazi.auth.provider.EmailAuthenticationProvider;
import org.lu.zhaodazi.auth.provider.WxAuthenticationProvider;
import org.lu.zhaodazi.user.service.TokenService;
import org.lu.zhaodazi.user.service.UserService;
import org.lu.zhaodazi.websocket.service.WebsocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    UserService userService;
    @Autowired
    WebsocketService websocketService;
    @Autowired
    TokenService tokenService;
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(AbstractHttpConfigurer::disable);
        return http.build()
                ;
    }

    @Bean
    public EmailAuthenticationProvider emailAuthenticationProvider() {
        return new EmailAuthenticationProvider(userService);
    }
    @Bean
    public WxAuthenticationProvider wxAuthenticationProvider(){
        return new WxAuthenticationProvider(websocketService,userService,tokenService);
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(emailAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(authenticationProviders);
        return authenticationManager;
    }
}