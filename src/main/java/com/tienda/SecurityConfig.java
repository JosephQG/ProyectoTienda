package com.tienda;

import com.tienda.domain.Ruta;
import com.tienda.service.RutaService;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            @Lazy RutaService rutaService)
            throws Exception {

        // Obtener las rutas desde la BD
        var rutas = rutaService.getRutas();

        http.authorizeHttpRequests(request -> {

            // Configurar rutas dinámicas según la tabla ruta
            for (Ruta ruta : rutas) {

                // Si no requiere rol → acceso público
                if (!ruta.isRequiereRol()) {
                    request.requestMatchers(ruta.getRuta())
                            .permitAll();
                } // Si requiere rol → validar el rol
                else {
                    request.requestMatchers(ruta.getRuta())
                            .hasRole(ruta.getRol().getRol());
                }
            }

            // Cualquier otra ruta requiere autenticación
            request.anyRequest().authenticated();
        });

        // Configurar Login
        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        );

        // Configurar Logout
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        );

        // Página para acceso denegado
        http.exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/acceso_denegado")
        );

        // Manejo de sesión
        http.sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );

        return http.build();
    }

    // Bean del encriptador
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configurar UserDetailsService
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build,
            @Lazy PasswordEncoder passwordEncoder,
            @Lazy UserDetailsService userDetailsService)
            throws Exception {

        build.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

}