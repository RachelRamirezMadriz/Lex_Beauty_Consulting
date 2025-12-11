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
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(requests -> {

            requests.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/img/**", "/favicon.ico").permitAll();
            requests.requestMatchers("/login", "/error", "/registro", "/nosotros", "/contacto").permitAll();
            requests.requestMatchers(HttpMethod.GET, "/").permitAll();

            // Catálogos visibles para todos (solo lectura)
            requests.requestMatchers(HttpMethod.GET, "/producto/disponibles", "/categoria/disponibles").permitAll();

            // Solo admin o vendedor
            requests.requestMatchers(HttpMethod.GET, "/producto/listado", "/categoria/listado")
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_VENDEDOR");
            requests.requestMatchers("/producto/**", "/categoria/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_VENDEDOR");
            requests.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
            requests.requestMatchers("/usuario/**").hasAuthority("ROLE_ADMIN");

            // Compatibilidad con rutas de vendedor
            requests.requestMatchers("/vendedor/**").hasAnyAuthority("ROLE_CLIENTE", "ROLE_VENDEDOR");

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
                .accessDeniedPage("/acceso-denegado")
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
