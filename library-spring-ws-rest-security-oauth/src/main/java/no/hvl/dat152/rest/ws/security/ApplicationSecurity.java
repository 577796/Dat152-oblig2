/**
 * 
 */
package no.hvl.dat152.rest.ws.security;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author tdoy
 */
@Configuration
@EnableMethodSecurity
public class ApplicationSecurity {
	
	@Autowired
	private AuthTokenFilter authTokenFilter;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf(csrf->csrf.disable());
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(authorize -> 
				authorize.anyRequest().authenticated());
		http.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwtconfig -> jwtconfig.jwtAuthenticationConverter(jwt -> {
					Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
					Collection<String> roles = realmAccess.get("roles");
					var grantedAuthorities = roles.stream()
							.map(role -> new SimpleGrantedAuthority(role))
							.collect(Collectors.toList());
					
					return new JwtAuthenticationToken(jwt,grantedAuthorities);
				})));
		http.addFilterAfter(authTokenFilter, BearerTokenAuthenticationFilter.class);
		
		return http.build();		
	}

	
}
