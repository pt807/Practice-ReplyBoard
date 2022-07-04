//package com.board_test.demo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
//
//@Configuration
//public class SecurityConfiguration {
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/h2-console/**").permitAll()
//                    .anyRequest().authenticated()
//                .and()
//                .csrf()
//                    .ignoringAntMatchers("/h2-console/**")
//                .and()
//                .headers()
//                    .addHeaderWriter(
//                            new XFrameOptionsHeaderWriter(
//                                    XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
//                            )
//                    );
//
//        http.headers().frameOptions().sameOrigin();
//
//        return http.build();
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
//    }
//}
