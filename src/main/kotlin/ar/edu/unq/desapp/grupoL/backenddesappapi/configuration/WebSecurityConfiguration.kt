package ar.edu.unq.desapp.grupoL.backenddesappapi.configuration

import ar.edu.unq.desapp.grupoL.backenddesappapi.jwt.AuthenticationEntryPointJwt
import ar.edu.unq.desapp.grupoL.backenddesappapi.jwt.RequestFilterJwt
import ar.edu.unq.desapp.grupoL.backenddesappapi.services.JwtUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var authenticationEntryPointJwt: AuthenticationEntryPointJwt

    @Autowired
    private lateinit var jwtUserDetailsService: JwtUserDetailsService

    @Autowired
    private lateinit var requestFilterJwt: RequestFilterJwt

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.csrf().disable()
            .cors().configurationSource(corsConfigurationSource())
            .and().authorizeRequests().antMatchers("/api/user/register/", "/api/user/register", "/api/user/login/", "/api/user/login", "/swagger-ui.html", "/v2/api-docs/**", "/**/swagger-resources/**", "/webjars/springfox-swagger-ui/**").permitAll()
            .and().authorizeRequests().anyRequest().authenticated()
            .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPointJwt)
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilterBefore(requestFilterJwt, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000", "https://localhost:3000", "https://frontend-desapp-grupol.herokuapp.com/", "https://frontend-desapp-grupol.herokuapp.com")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "OPTIONS")
        configuration.allowCredentials = true
        configuration.addAllowedHeader("*")
        configuration.addExposedHeader("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}