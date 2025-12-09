package LexBeautyConsulting.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(requests -> {

            // Recursos públicos
            requests.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll();
            requests.requestMatchers("/login", "/error", "/registro").permitAll();

            // Rutas por rol
            requests.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
            requests.requestMatchers("/vendedor/**").hasAuthority("ROLE_CLIENTE");
            requests.requestMatchers("/usuario/**").hasAuthority("ROLE_USER");

            // Página principal
            requests.requestMatchers("/").authenticated();

            // Todo lo demás requiere login
            requests.anyRequest().authenticated();
        });

        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        ).logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/acceso_denegado")
        ).sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build,
                                  @Lazy PasswordEncoder passwordEncoder,
                                  @Lazy UserDetailsService userDetailsService) throws Exception {

        build.userDetailsService(userDetailsService)
             .passwordEncoder(passwordEncoder);
    }
}