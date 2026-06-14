package com.example.admin_web_app.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
class SecurityConfig {
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Value("${bookstore.public-url:http://localhost:8989}")
    private String publicUrl;

    SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(c -> c.requestMatchers("/js/*", "/css/*", "/images/*", "/error", "/webjars/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .cors(CorsConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler(oidcLogoutSuccessHandler()));
        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            private final SimpleUrlLogoutSuccessHandler delegate = new SimpleUrlLogoutSuccessHandler();

            @Override
            public void onLogoutSuccess(
                    HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                    throws IOException, jakarta.servlet.ServletException {
                ClientRegistration registration = clientRegistrationRepository.findByRegistrationId("bookstore-webapp");
                if (registration != null
                        && authentication != null
                        && authentication.getPrincipal() instanceof OidcUser oidcUser) {
                    String authorizationUri = registration.getProviderDetails().getAuthorizationUri();
                    String logoutUrl = authorizationUri.replace("/auth", "/logout");
                    String idToken = oidcUser.getIdToken().getTokenValue();
                    // Use the public-facing URL so Keycloak can redirect the browser back.
                    String redirectUri = publicUrl + "/admin";
                    String targetUrl = logoutUrl + "?id_token_hint=" + idToken + "&post_logout_redirect_uri="
                            + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
                    response.sendRedirect(targetUrl);
                } else {
                    delegate.onLogoutSuccess(request, response, authentication);
                }
            }
        };
    }
}
