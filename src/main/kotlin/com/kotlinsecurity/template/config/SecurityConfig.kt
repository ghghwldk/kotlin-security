package com.kotlinsecurity.template.config

import com.kotlinsecurity.template.jwt.JwtAccessDeniedHandler
import com.kotlinsecurity.template.jwt.JwtAuthenticationEntryPoint
import com.kotlinsecurity.template.jwt.JwtFilter
import com.kotlinsecurity.template.jwt.TokenProvider
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Configuration

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(private val tokenProvider: TokenProvider,
                     private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
                     private val jwtAccessDeniedHandler: JwtAccessDeniedHandler) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // h2 database 테스트가 원활하도록 관련 API 들은 전부 무시
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring()
                    .antMatchers("/h2-console/**", "/favicon.ico")
        }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}