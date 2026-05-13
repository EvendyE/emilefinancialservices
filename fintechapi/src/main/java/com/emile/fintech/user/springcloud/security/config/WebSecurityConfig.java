package com.emile.fintech.user.springcloud.security.config;

//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

//@Configuration // annotation for classes in which beans are defined.
//@EnableWebSecurity
//public class WebSecurityConfig {

//	@Autowired
//	UserDetailsService userDetailsService;

//	@Bean // prompts Spring to locate the dependencies necessary for this class type and exposes out those dependencies to be automatically injected into any instance of this class type marked w/ @Autowired.
//	SecurityContextRepository securityContextRepository() {
//		return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(),
//				new HttpSessionSecurityContextRepository());
//	}

//	@Bean
//	JwtAuthenticationConverter jwtAuthConverter() {
//		
//		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
//		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
//	
//		
//		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();	
//		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
//		
//		return jwtAuthenticationConverter;
//		
//	}
	
//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;
	
//	@Bean
//	AuthenticationManager authManager() throws Exception {
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // an AuthenticationProvider implementation.
//		provider.setUserDetailsService(userDetailsService);
//		provider.setPasswordEncoder(passwordEncoder);
//		return new ProviderManager(provider); // ProviderManager is the default implementation of AuthenticationManager. It delegates the authentication process to a list of AuthenticationProvider instances.
//	}

//	@Bean
//	@Order(Ordered.LOWEST_PRECEDENCE)
//	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//		http.authorizeHttpRequests((authorize) -> {
//				authorize.requestMatchers(HttpMethod.POST, "/user/id", "/accounts/account").hasAnyRole("USER", "ADMIN"); // authorize.requestMatchers specifies which RESTful end-point is accessible after authentication. .hasAnyRole/hasRole specifies who among the authenticated can have access to RESTful end-points as according to their role(s).
//				authorize.requestMatchers(HttpMethod.GET, "/users/encodePassword").permitAll();
//		});
		
		// http.formLogin(Customizer.withDefaults()); // sets up the default /login endpoint.
		
		// CORS configuration that could have optionally been configured on the CrossOrigin annotation instead. 
//		http.cors((corsCustomizer) -> {
//			CorsConfigurationSource configurationSource = request -> {
//				CorsConfiguration corsConfiguration = new CorsConfiguration();
//				corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000")); // HTTP Header which specifies which domain can make HTTP requests from outside this domain. 
//				corsConfiguration.setAllowedMethods(List.of("POST", "GET")); // HTTP Header which specifies which type of HTTP Method is allowed to be communicated w/ by HTTP requests from outside this domain.
//				corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-CSRF-TOKEN")); // cannot be set to "*" when setAllowedCredentials is set to true.
//				corsConfiguration.setAllowCredentials(true); // informs the browsers whether the server allows cross-origin HTTP requests to include credentials. Credentials are cookies, TLS client certificates, or authentication headers containing a username and password.
//				return corsConfiguration;
//			};
//			corsCustomizer.configurationSource(configurationSource);
//		}).sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        .authenticationProvider(new DaoAuthenticationProvider())
//        .addFilterBefore(new UsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

//		http.oauth2ResourceServer(oauth->oauth.jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthConverter())));
//		
//		http.csrf(Customizer.withDefaults());
		
		// generates a CSRF token and sets it as a cookie with HttpOnly set to false so it can be accessed by JavaScript if needed.
//		http.csrf((csrfCustomizer) -> 
//		{
//			csrfCustomizer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//		});
		
//		http.csrf((csrfCustomizer) -> {
//			csrfCustomizer.disable();
//		});
		

//		http.csrf((csrfCustomizer) -> {
//			RequestMatcher requestMatchers = new RegexRequestMatcher("/login", "POST");
//			csrfCustomizer.ignoringRequestMatchers(requestMatchers);
//		});
		
//		http.securityContext((securityContextCustomizer) -> {
//				securityContextCustomizer.securityContextRepository(new DelegatingSecurityContextRepository(
//					new RequestAttributeSecurityContextRepository(),
//					new HttpSessionSecurityContextRepository()
//				));
//		});
		
//		http.securityContext(context -> context.requireExplicitSave(true)); // enables credentials to be saved in Security Context.

//		return http.build();
//	}
//}
