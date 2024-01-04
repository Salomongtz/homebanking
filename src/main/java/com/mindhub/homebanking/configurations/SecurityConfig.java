package com.mindhub.homebanking.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/web/*", "/web/assets" +
                        "/images/**", "/web/assets/styles/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/clients/current/accounts", "/api/transactions", "/api/clients" +
                        "/current/cards").hasAuthority("CLIENT")
                .requestMatchers(HttpMethod.GET, "/api/clients/current", "/api/clients/current/*", "/web" +
                        "/assets/pages/**").hasAuthority("CLIENT")
                .requestMatchers(HttpMethod.GET, "/api/clients/", "web/**").hasAuthority("ADMIN")
                .requestMatchers("/h2-console/**").hasAuthority("ADMIN")
                .anyRequest().denyAll());

        http.csrf(AbstractHttpConfigurer::disable);

        http.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::disable));

        http.formLogin(formLogin -> formLogin
                .loginPage("/index.html")
                .loginProcessingUrl("/api/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    clearAuthenticationAttributes(request);
                    System.out.println("Hola" + authentication.getName());
                })
                .failureHandler((request, response, exception) -> response.sendError(401))
                .permitAll());

        http.rememberMe(Customizer.withDefaults());

        http.logout(logout -> logout.logoutUrl("/api/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()));

        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/web/index.html")));

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
