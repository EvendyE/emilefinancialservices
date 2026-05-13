package com.emile.fintech.user.springcloud.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.reactive.function.client.WebClient;

@EnableWebSecurity
@Configuration
public class DefaultSecurityConfig { // this configuration class is only concerned w/ the security of the endpoints, /oauth2/authorize and /oauth2/token.

	@Autowired
	UserDetailsService userDetailsService;
	
	@Bean
	public ReactiveOAuth2AuthorizedClientService authorizedclientservice2(ReactiveClientRegistrationRepository clientRegistrationRepository) {
	    return new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
	}
	
	@Bean
	public WebClient webClient(ReactiveClientRegistrationRepository clientRegistrationRepository,
	        ReactiveOAuth2AuthorizedClientService authorizedclientservice2) {

	    ReactiveOAuth2AuthorizedClientManager authorizedClientManager = 
	        new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
	            clientRegistrationRepository, 
	            authorizedclientservice2
	        );

	    ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = 
	        new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

	    oauth.setDefaultClientRegistrationId("my-client-registration-id");

	    return WebClient.builder()
	        .filter(oauth)
	        .build();
	}
	
	@Bean // prompts Spring to locate the dependencies necessary for this class type and exposes out those dependencies to be automatically injected into any instance of this class type marked w/ @Autowired.
	SecurityContextRepository securityContextRepository() {
		return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(),
				new HttpSessionSecurityContextRepository());
	}
	
	@Bean
	BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	JwtAuthenticationConverter jwtAuthConverter() {
		
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
	
		
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();	
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		
		return jwtAuthenticationConverter;
		
	}
	
//	@Bean
//	AuthenticationManager authManager() throws Exception {
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // an AuthenticationProvider implementation.
//		provider.setUserDetailsService(userDetailsService);
//		provider.setPasswordEncoder(bcryptPasswordEncoder());
//		return new ProviderManager(provider); // ProviderManager is the default implementation of AuthenticationManager. It delegates the authentication process to a list of AuthenticationProvider instances.
//	}
	
	@Bean
	 SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
		.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // all OPTIONS requests are permitted, which is essential for handling CORS pre-flight requests; every other request will be subject to authentication as defined.	
		.requestMatchers("/login**", "/oauth2/**").permitAll() // allow unauthenticated access to login and OAuth2 endpoints.
		.requestMatchers(HttpMethod.POST, "/accounts/account").hasAnyRole("USER", "ADMIN") // Restrict POST requests to these endpoints to users with "USER" or "ADMIN" roles.
		.anyRequest().authenticated()) // require authentication for all other requests besides requests to endpoints configured with permitAll().
		.formLogin(Customizer.withDefaults()); // requests are authenticated through formLogin, the default /login endpoint.
		
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
		
		http.securityContext(context -> context.requireExplicitSave(true)); // enables credentials to be saved in Security Context.
		
		return http.build();
	}
	
	@Bean 
	public CorsFilter corsFilter() { 
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); 
		CorsConfiguration config = new CorsConfiguration(); 
		config.setAllowCredentials(true); // informs the browsers whether the server accepts credentials from cross-origin HTTP requests. Credentials are cookies, TLS client certificates, or authentication headers.
		config.addAllowedOrigin("https://localhost:3000"); // HTTP Header which specifies which domain can make HTTP requests from outside this domain. 
		config.addAllowedHeader("*"); // HTTP Header which specifies which type of HTTP Method is allowed to be communicated w/ by HTTP requests from outside this domain. Options must be allowed in order for successful cross-origin requests b/c browsers make Preflight requests, an OPTIONS request to the server to check if the actual request is safe to send, before every cross-origin request.
		config.addAllowedMethod("*"); 
		source.registerCorsConfiguration("/**", config); 
		return new CorsFilter(source); 
	}
	
}
