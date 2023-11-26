package vn.edu.iuh.fit.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    //inMemoryAuthentication
//    @Autowired
//    public void globalConfig(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser(User
//                        .withUsername("hiep")
//                        .password(passwordEncoder.encode("hiep"))
//                        .roles("USER")
//                        .build())
//                .withUser(User
//                        .withUsername("teo")
//                        .password(passwordEncoder.encode("teo"))
//                        .roles("TEO")
//                        .build())
//                .withUser(User
//                        .withUsername("admin")
//                        .password(passwordEncoder.encode("admin"))
//                        .roles("ADMIN")
//                        .build()
//                )
//        ;
//    }

    @Autowired
    public void globalConfig(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder,DataSource dataSource) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .withUser(User
                        .withUsername("hiep")
                        .password(passwordEncoder.encode("hiep"))
                        .roles("USER")
                        .build())
                .withUser(User
                        .withUsername("teo")
                        .password(passwordEncoder.encode("teo"))
                        .roles("TEO")
                        .build())
                .withUser(User
                        .withUsername("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles("ADMIN")
                        .build()
                )
        ;
    }

    @Bean
    public CorsConfigurationSource corsConfiguration(){
        return request -> {
            org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);

            return configuration;
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/home", "/", "/index").permitAll()
                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .requestMatchers("/api", "api/**").hasAnyRole("ADMIN","USER","TEO")
                .requestMatchers("/api/v1/auth/**", "/v2/api-docs/**", "/v3/api-docs/**",
                        "/swagger-resources/**", "/swagger-ui/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
        );

        //h2-console
        httpSecurity
                .csrf(csrf->csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers-> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable) //for POST/PUT/DELETE swagger
                .httpBasic(Customizer.withDefaults());

        //for POST/PUT/DELETE swagger
        httpSecurity.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfiguration()));
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
