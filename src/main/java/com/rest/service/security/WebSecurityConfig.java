package com.rest.service.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.rest.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebMvcConfigurationSupport {

	@Autowired
	private UserService userDetailsService;

	@Autowired
	PasswordEncoder passwordEncoder;

	public WebSecurityConfig(UserService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Value("${jwt.public.key}")
	RSAPublicKey key;

	@Value("${jwt.private.key}")
	RSAPrivateKey priv;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		System.out.println(http);
		// @formatter:off
		
		
//		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//		authenticationManagerBuilder.authenticationProvider(authProvider());
//		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        // Get AuthenticationManager
//        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
//        http.authenticationManager(authenticationManager);
        http.cors();
        http.headers().frameOptions().disable();
		http
				.authorizeHttpRequests((authorize) -> authorize
						.antMatchers("/images/**","/h2-console/**/**").permitAll()
						.antMatchers(HttpMethod.GET,"/users").permitAll()
						.antMatchers(HttpMethod.POST,"/register").permitAll()
						.anyRequest().authenticated()
				)
				.csrf((csrf) -> csrf.ignoringAntMatchers("/token","/register"))
				.httpBasic(Customizer.withDefaults())
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling((exceptions) -> exceptions
						.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())  //
						.accessDeniedHandler(new BearerTokenAccessDeniedHandler())
				); 
		// @formatter:on
		return http.build();
	}

//	@Bean
//	UserDetailsService users() {
//		// @formatter:off
////		System.out.println("== ");
//		return new InMemoryUserDetailsManager(
//			User.withUsername("user")
//				.password("{noop}pass")
//				.authorities("app")
//				.build()
//		);
//		// @formatter:on
//	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(this.key).build();
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

	}

	@Bean
	JwtEncoder jwtEncoder() {

		JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}

}