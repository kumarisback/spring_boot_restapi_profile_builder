package com.rest.service.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.rest.service.UserService;
import com.rest.service.exception.AccessDeniedHandlerJwt;
import com.rest.service.exception.AuthenticationEntryPointJwt;
import com.rest.service.exception.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig extends WebMvcConfigurationSupport {

	@Autowired
	private UserService userDetailsService;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationEntryPointJwt authExceptionEntryPoint;
	
	@Autowired
	AccessDeniedHandlerJwt  accessDeniedHandlerJwt;

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
				.authorizeHttpRequests((authorize) -> {
					try {
						authorize
								.antMatchers("/images/**","/h2-console/**/**").permitAll()
								.antMatchers(HttpMethod.GET,"/users").permitAll()
								.antMatchers(HttpMethod.GET,"/profile/**").permitAll()
								.antMatchers(HttpMethod.POST,"/register").permitAll()
								.anyRequest().authenticated();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				)
				.csrf((csrf) -> csrf.ignoringAntMatchers("/token","/register"))
				.httpBasic(Customizer.withDefaults())
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling((exceptions) -> exceptions
						.authenticationEntryPoint(	
								(request, response, e) -> {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("ID", "invalid");
							jsonObject.put("username",null);
							String json = String.format("{\"message\": \"%s\"}", jsonObject);
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(json);  }
								)
						.accessDeniedHandler(accessDeniedHandlerJwt)
				); 
		// @formatter:on
		return http.build();
	}

              
//}
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
		public CorsConfigurationSource corsConfigurationSource() {
		    final CorsConfiguration configuration = new CorsConfiguration();
		    configuration.setAllowedOrigins(Arrays.asList("*"));
		    configuration.setAllowedMethods(Arrays.asList("HEAD",
		            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		    
//		    configuration.setAllowCredentials(true);  this is used when only specific domain you want in allowed origin
		    configuration.setAllowedHeaders(Arrays.asList("*"));
		    configuration.setExposedHeaders(Arrays.asList("Authorization"));
		    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    source.registerCorsConfiguration("/**", configuration);
		    return (CorsConfigurationSource) source;
		}
	 
	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(this.key).build();
	}

//	@Override
//	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//
//	}

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


	@Bean
	public AuthenticationEventPublisher authenticationEventPublisher
	        (ApplicationEventPublisher applicationEventPublisher) {
	    return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
	}

}