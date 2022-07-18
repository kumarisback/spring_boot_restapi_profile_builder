package com.rest.service.controller;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.rest.service.UserService;
import com.rest.service.entitys.UserData;

//@CrossOrigin
@RestController
public class TokenController {

	@Autowired
	JwtEncoder encoder;

	@Autowired
	UserService userService;

	@PostMapping("/token")
	public ResponseEntity<JSONObject> token(Authentication authentication) {
		Instant now = Instant.now();
		long expiry = 36000L;
		String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("self").issuedAt(now).expiresAt(now.plusSeconds(expiry))
				.subject(authentication.getName()).claim("scope", scope).build();
		UserData user = userService.findByUsername(authentication.getName());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ID", user.getId());
		jsonObject.put("username", user.getEmail());
		jsonObject.put("token", this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
		return ResponseEntity.ok(jsonObject);

//		return ResponseEntity.ok( new Object[] {this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), user.getId().toString()});
	}

}