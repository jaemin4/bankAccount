package com.pro.config;


import com.pro.securityAuth.RefreshTokenRepository;
import com.pro.filter.SecurityJwtFilter;
import com.pro.filter.SecurityLoginFilter;
import com.pro.filter.SecurityLogoutFilter;
import com.pro.util.FilterUtil;
import com.pro.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final FilterUtil filterUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/bank/**").permitAll()
                .requestMatchers("/user/signup/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/reissue").permitAll()
                .anyRequest().authenticated() );

        http.logout((logout) -> logout
                .logoutSuccessUrl("/logout")
        );
        http.addFilterBefore(new SecurityJwtFilter(jwtUtil,filterUtil), SecurityLoginFilter.class);

        http.addFilterAt(new SecurityLoginFilter(
                authenticationManager(authenticationConfiguration), jwtUtil,refreshTokenRepository),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new SecurityLogoutFilter(jwtUtil), LogoutFilter.class);

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
