package config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/users").permitAll()  // 로그인 및 회원가입 경로에 대해 인증 없이 접근 허용
                        .anyRequest().authenticated()  // 나머지 요청에 대해 인증 요구
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 필요한 경우 세션 생성
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")  // 커스텀 로그인 페이지 설정
                        .permitAll()  // 로그인 페이지 접근 허용
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")  // 로그아웃 URL 설정
                        .logoutSuccessUrl("/auth/login?logout")  // 로그아웃 후 리다이렉트 URL
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
