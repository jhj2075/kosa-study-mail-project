package futurenet.fullstack.reservation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      AuthenticationEntryPoint authenticationEntryPoint
  ) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable())
        .logout(logout -> logout.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/css/**",
                "/favicon.ico"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(authenticationEntryPoint)
        )
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        )
        .build();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint(
      @Value("${auth.login-url:http://localhost:8081/login}") String loginUrl
  ) {
    return new LoginUrlAuthenticationEntryPoint(loginUrl);
  }
}
