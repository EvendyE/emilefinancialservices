package com.emile.fintech.user.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

//import com.emile.fintech.user.model.User;
import com.emile.fintech.user.repos.UserDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode; 
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController // defines this class as a RESTful controller.
//@RequestMapping("/user") // defines the URI path the controller should be mapped to.
//@CrossOrigin // gets the server and spring to permit the front-end (running on port 3000) and the back-end (running on port 8080) to communicate w/ each other.
public class UserRestController {

	@Autowired
	private UserDataRepository userRepository;

	@Autowired
	private RestTemplate restTemplate; /* allows requests to be made to other RESTful end points. RestTemplate must be
										* a method defined in the application class and annotated w/ the bean
										* annotation. */
	
	@Autowired
    private WebClient.Builder webClientBuilder;

//	@Autowired
//  private HttpServletRequest request; // retrieves the details of the latest HTTP request made to this server.
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	static ResponseEntity<String> csrfResponse;
	
	// SecurityContextHolder.getContext().getAuthentication().getName() retrieves the username of the currently authenticated user.
	// restTemplate.postForObject() makes api calls to RESTful endpoints.
	@GetMapping("/login/oauth2/code/my-client-registration-id")
	public List<?> getIdNo(@RequestParam("code") String code) throws JsonMappingException, JsonProcessingException {
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>(); 
		formData.add("grant_type", "authorization_code"); 
		formData.add("code", code); // extracted authorization code from the URI query parameter, https://localhost:443/emilefinancialservices/login/oauth2/code/my-client-registration-id?code=rKfm_F9akM-wWyWSLg5qP8PbQBfQZ03E_2VTrkSzFnJD0-6MEOjFWaUXe--KVfmUIY3UvclC3lSQibGVAvATrhgL0LGqVlTqJVXcXArPq_SsrfTE39IDFGn63pG7Ad9Q, used to request an access token from the token endpoint.
		formData.add("client_id", "client"); 
		formData.add("client_secret", "9999");
		formData.add("redirect_uri", "https://localhost:443/emilefinancialservices/login/oauth2/code/my-client-registration-id");
		
		WebClient webClient = WebClient.builder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			    .build();

		String accessTokenResponse = webClient.post()
			    .uri("https://localhost:443/emilefinancialservices/oauth2/token")
			    .body(BodyInserters.fromFormData(formData))
			    .retrieve()
			    .bodyToMono(String.class)
			    .block();

		ObjectMapper objectMapper = new ObjectMapper(); 
		JsonNode jsonNode = objectMapper.readTree(accessTokenResponse); 
		String accessToken = jsonNode.get("access_token").asText();
	  	HttpHeaders headers = new HttpHeaders();
	  	headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(userRepository.findIdByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).toString(), headers);
		try {
			return restTemplate.postForObject("https://localhost:443/emilefinancialservices/accounts/account", entity, List.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN); // sets the HTTP status of the response to the client, to 403.
		
		return null;
	}
}
