/**
 * 
 */
package no.hvl.dat152.rest.ws.security;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import no.hvl.dat152.rest.ws.security.crypto.Certificates;
import no.hvl.dat152.rest.ws.security.crypto.KeyStores;
import no.hvl.dat152.rest.ws.security.service.UserDetailsImpl;

/**
 * @author tdoy
 */
@Component
public class JwtTokenUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
	
	@Value("${token.expiry.duration}") 
	private long EXPIRE_DURATION;
	
//	@Value("${rest.jwt.secret}") 
	private static final byte[] SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS384).getEncoded(); // can be shared in real-time with other replicated servers
	
	@Value("classpath:keys/elibcert2.cer")
	private Resource publicKeyFile;
	
	@Value("classpath:keys/elib_keystore2")
	private Resource privateKeyFile;
	
	private PublicKey pkey;
	
	public String createAccessToken(Authentication authentication) {
		
		return createToken(authentication, key(), SignatureAlgorithm.HS384);
	}
	
	public String createAccessTokenWithPublicKey(Authentication authentication) {

		PrivateKey privKey = null;
		try {
			
			String privateKeystore = privateKeyFile.getFile().getCanonicalPath();
			privKey = KeyStores.getPrivateKeyFromKeyStore(privateKeystore, "elibapi1", "elibapi");
			
		} catch (IOException | UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		
		return createToken(authentication, privKey, SignatureAlgorithm.RS384);
	}
	
	private Key key() {
		return Keys.hmacShaKeyFor(SECRET_KEY);
	}
	
	private String createToken(Authentication authentication, Key key, SignatureAlgorithm alg) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		
		return Jwts.builder()
				.setSubject(userPrincipal.getEmail())
				.setIssuer("DAT152-Lecturer@TDOY")
				.claim("firstname", userPrincipal.getFirstname())
				.claim("lastname", userPrincipal.getLastname())
				.claim("roles", userPrincipal.getAuthorities().stream()
						.map(role -> role.getAuthority())
						.collect(Collectors.toList()))
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
				.signWith(key, alg)
				.compact();
	}
	
	public boolean validateAccessToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parse(token);
			return true;
		}catch(MalformedJwtException ex) {
			LOGGER.error("JWT is invalid!", ex.getMessage());
		}catch(UnsupportedJwtException ex) {
			LOGGER.error("JWT is unsupported!", ex.getMessage());
		}catch(ExpiredJwtException ex) {
			LOGGER.error("JWT token is expired!", ex.getMessage());
		}catch(IllegalArgumentException ex) {
			LOGGER.error("JWT token has improper argument!", ex.getMessage());
		}
		
		return false;

	}
	
	public boolean validateAccessTokenWithPublicKey(String token) throws IOException {
		pkey = Certificates.getPublicKey(publicKeyFile.getFile().getCanonicalPath());
		try {
			Jwts.parserBuilder().setSigningKey(pkey).build().parse(token);
			return true;
		}catch(MalformedJwtException ex) {
			LOGGER.error("JWT is invalid!", ex.getMessage());
		}catch(UnsupportedJwtException ex) {
			LOGGER.error("JWT is unsupported!", ex.getMessage());
		}catch(ExpiredJwtException ex) {
			LOGGER.error("JWT token is expired!", ex.getMessage());
		}catch(IllegalArgumentException ex) {
			LOGGER.error("JWT token has improper argument!", ex.getMessage());
		}
		
		return false;

	}
	
	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}
	
	public Claims parseClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(SECRET_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public Claims parseClaimsWithPublicKey(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(pkey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	
	
}
