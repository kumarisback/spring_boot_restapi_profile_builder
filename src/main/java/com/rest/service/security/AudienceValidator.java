//package com.rest.security;
//
////import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties.Jwt;
//import org.springframework.security.oauth2.core.OAuth2Error;
//import org.springframework.security.oauth2.core.OAuth2TokenValidator;
//import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
//import org.springframework.security.oauth2.jwt.Jwt;
//
//import com.rest.security.WebSecurityConfig.AudienceValidator;
//
//
//static class AudienceValidator implements OAuth2TokenValidator<Jwt> {
//    OAuth2Error error = new OAuth2Error("custom_code", "session expire", null);
//
//    @Override
//    public OAuth2TokenValidatorResult validate(Jwt jwt) {
//    	
//    	System.out.println(jwt.toString());
//        if (jwt.getAudience().contains("messaging")) {
//        	
//            return OAuth2TokenValidatorResult.success();
//        } else {
//            return OAuth2TokenValidatorResult.failure(error);
//        }
//    }
//}
//
//
//OAuth2TokenValidator<Jwt> audienceValidator() {
//    return new AudienceValidator();
//}