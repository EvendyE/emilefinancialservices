package com.emile.fintech.user.springcloud.security.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.PasswordLookup;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfigurartion {
	
	private static final String ROLES_CLAIM = "roles";

	@Autowired
	private UserDetailsService userDetailsService;

	@Value("${keyFile}")
	private String keyFile;

	@Value("${password}")
	private String password;

//	@Value("${alias}")
//	private String alias;
	
	@Value("${providerUrl}")
	private String providerUrl;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	@Value("${spring.security.oauth2.client.registration.my-client-registration-id.redirect-uri}") 
//	private String redirectUriTemplate;
	
	@Bean
	AuthenticationManager authManager() throws Exception {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // an AuthenticationProvider implementation.
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(provider); // ProviderManager is the default implementation of AuthenticationManager. It delegates the authentication process to a list of AuthenticationProvider instances.
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE) // a filter chain marked w/ this annotation will be the first to configure authorization.
	public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception { // the authorization server is only concerned w/ the security of the endpoints, /oauth2/authorize and /oauth2/token, it exposes by default.
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		return http.userDetailsService(userDetailsService).formLogin(Customizer.withDefaults()).build(); // unauthenticated user requests to its endpoints are redirected to the login page. 
	}
	
	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource); // creates an instance of JwtDecoder.
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource()
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		JWKSet jwkSet = buildJWKSet();  // jwkSet is initialized to the instance of JWKSet loaded w/ the public and private keys, returned by buildJWKSet().
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet); /* expressed as a lambda expression b/c JWKSource is a functional interface, an interface w/ only one 
		 																	  * function; optionally could have been written as an instance of JWKSource implemented as a
																			  * callback handler w/ a defined, overridden get() which returns List<JWK> and accepts variables of
																			  * type JWKSelector and SecurityContext as parameters. */ 
	}

	private JWKSet buildJWKSet() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore keyStore = KeyStore.getInstance("pkcs12");
		try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(keyFile);) { // try is used a private resource block to automatically close the input stream. Input stream to load up the keyFile. getResourceAsStream b/c the jks is on the class path.
			keyStore.load(resourceAsStream, password.toCharArray()); // keyStore to load up the public and private key; toCharArray() b/c the load method from the KeyStore instance expects the name of the key in the form of an array of characters, not a String.

			return JWKSet.load(keyStore, new PasswordLookup() { /* dynamically created JWK set; new PasswordLookup instance is implemented as a callback handler w/in the method 
			 													 * parameter list alongside the KeyStore instance. lookupPassword() retrieves the password corresponding to the 
																 * alias, which JWKSet.load() needs before it can load up the instance of KeyStore. */ 

				@Override
				public char[] lookupPassword(@Value("${alias}") String alias) {
					return password.toCharArray();
				}
			});
		}

	}
	
	@Bean
	AuthorizationServerSettings authServerSettings() { // bean to configure the issuer URL which will be apart of the JWT token and serve as its unique identifier.
		return AuthorizationServerSettings.builder().issuer(providerUrl).build(); // the issuer URL is configured using providerURL. 
	
	}
	
	/* registers the client application (e.g., the front-end) to the authorization server to 
	 * create the client application's client id and secret needed in order to receive the
	 * JWT token and refresh token from the authorization server after the user authenticates
	 * themselves to the authorization server and authorizes the client application to 
	 * receive the token so that the client application can perform operations on behalf of
	 * the user. The refresh token is a token the client application sends to the
	 * authorization server after the JWT token expires, in order to receive a new JWT token 
	 * w/o the need for the user to authenticate themselves again. */
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
//		String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(); 
//		String redirectUri = String.format(redirectUriTemplate, baseUrl, "my-client-registration-id");
//		System.out.println("baseUrl: " + baseUrl + ", redirectUri: " + redirectUri);

		RegisteredClient registeredClient = RegisteredClient.withId("my-client-registration-id")
				.clientId("client")
				.clientSecret(passwordEncoder.encode("9999"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("https://localhost:443/emilefinancialservices/login/oauth2/code/my-client-registration-id")
				.scope("read")
				.scope("write").tokenSettings(tokenSettings()).build();
		return new InMemoryRegisteredClientRepository(registeredClient);
	}
	
	@Bean
	public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository() {
	    ClientRegistration registeredClient = ClientRegistration
	        .withRegistrationId("my-client-registration-id")
	        .clientId("client")
	        .clientSecret(passwordEncoder.encode("9999"))
	        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
	        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
	        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
	        .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}") // {baseUrl} and {registrationId} are placeholders that will be automatically replaced by Spring Security with the appropriate values at runtime. {baseUrl} will be your application's base URL. {registrationId} will be the identifier of your client registration.
	        .authorizationUri("https://localhost:443/emilefinancialservices/oauth2/authorize")
	        .tokenUri("https://localhost:443/emilefinancialservices/oauth2/token")
	        .jwkSetUri("https://localhost:443/emilefinancialservices/oauth2/jwks")
	        .scope("read", "write")
	        .build();
	    return new InMemoryReactiveClientRegistrationRepository(registeredClient);
	}

	@Bean
	public TokenSettings tokenSettings() {
		return TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(30l)).build();
	}
	
	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
		return context -> { // context is the JWT Token
			if(context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) { // if the JWT token is of type Access Token...
				Authentication principal = context.getPrincipal(); // getPrincipal() retrieves the security information and user details which is then assigned to the local variable, principal.
				Set<String> authorities = principal.getAuthorities().stream() // getAuthorities().stream retrieves all the role(s) a particular user has.
				.map(GrantedAuthority::getAuthority) // b/c each role will be of type GrantedAuthority, the getAuthority method from the GrantedAuthority class will convert the role(s) to Strings.
				.collect(Collectors.toSet()); // as each role is streamed and mapped, they are stored into the same set which then is assigned to the local variable, authorities.
				context.getClaims().claim("roles", authorities); // JWT tokens are composed of JSON objects called claims; getClaims() retrieves the claims, and claim() appends a key and value pair to one of the claims.              
			}
		};
	}

}
