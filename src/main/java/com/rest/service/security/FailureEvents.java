package com.rest.service.security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;

@Component
public class FailureEvents  {
	@EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent badCredentials) {
		System.out.println("hi");
		if (badCredentials.getAuthentication() instanceof BearerTokenAuthenticationToken) {
		    System.out.println(badCredentials.getAuthentication());
        }
    }
	
	 @EventListener
	    public void onFailure(AbstractAuthenticationFailureEvent failures) {
		 if(failures.getAuthentication().getCredentials()!=null)
			 throw new InvalidBearerTokenException("token is expire");
		 else {
			 throw new BadCredentialsException("Invalid credentials");
		 }
	    }
}