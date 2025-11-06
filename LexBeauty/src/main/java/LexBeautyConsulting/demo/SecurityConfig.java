
package LexBeautyConsulting.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class SecurityConfig {
    @Bean
    public SecurityFilterChain filtroDeSeguridad(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para pruebas locales
            .authorizeHttpRequests(autorizacion -> autorizacion
                .requestMatchers("/auth/login", "/", "/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/cliente/**").hasAuthority("CLIENTE")
                .anyRequest().authenticated()
            )
            .formLogin(formulario -> formulario
                .loginPage("/auth/login")                
                .loginProcessingUrl("/auth/login")       
                .defaultSuccessUrl("/auth/login", true)  
                .failureUrl("/auth/login?error=true")    
                .permitAll()
            )
            .logout(cerrarSesion -> cerrarSesion
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .permitAll());

        return http.build();
    }

     @Bean
    public PasswordEncoder codificadorDeContrasenas() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService servicioDeUsuarios(PasswordEncoder codificador) {
        UserDetails administrador = User.builder()
                .username("admin@lexbeauty.com")
                .password(codificador.encode("1234"))
                .authorities("ADMIN")
                .build();

        UserDetails cliente = User.builder()
                .username("cliente@lexbeauty.com")
                .password(codificador.encode("abcd"))
                .authorities("CLIENTE")
                .build();

        return new InMemoryUserDetailsManager(administrador, cliente);
    }
}
    

