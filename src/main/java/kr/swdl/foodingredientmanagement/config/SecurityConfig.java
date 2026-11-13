package kr.swdl.foodingredientmanagement.config;

import kr.swdl.foodingredientmanagement.security.AppAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AppAccessDeniedHandler AppAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(
                                "/loginView",
                                "/join",
                                "/css/**",
                                "/js/**",
                                "/vendor/**",
                                "/images/**"
                        ).permitAll()

                        .requestMatchers("/hq/admin/**").hasRole("HQ_ADMIN")
                        .requestMatchers("/hq/**").hasAnyRole("HQ", "HQ_ADMIN")
                        .requestMatchers("/store/**").hasAnyRole("HQ", "HQ_ADMIN","STORE")
                        .anyRequest().authenticated()
                );
        http.formLogin(form ->
                form.loginPage("/loginView")
                        .failureUrl("/loginView?error=true")
                        .loginProcessingUrl("/login")
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/",true)
                        .permitAll()
        );
        http.exceptionHandling(exception ->
                exception.accessDeniedHandler(AppAccessDeniedHandler)
        );
        return http.build();
    }
}
