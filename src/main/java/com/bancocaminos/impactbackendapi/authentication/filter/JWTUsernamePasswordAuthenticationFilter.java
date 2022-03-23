package com.bancocaminos.impactbackendapi.authentication.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bancocaminos.impactbackendapi.authentication.AuthenticationConfigConstants;
import com.bancocaminos.impactbackendapi.core.exception.authentication.UnableToAuthenticateUserException;
import com.bancocaminos.impactbackendapi.twofa.security.CustomAuthenticationProvider;
import com.bancocaminos.impactbackendapi.user.rest.model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
        private static Logger LOGGER = LogManager.getLogger(JWTUsernamePasswordAuthenticationFilter.class);
        private final AuthenticationManager authenticationManager;
        private final CustomAuthenticationProvider customAuthenticationProvider;

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                        throws AuthenticationException {
                try {
                        UserModel creds = new ObjectMapper()
                                        .readValue(request.getInputStream(), UserModel.class);
                        customAuthenticationProvider.validateVerificationCode(creds);
                        return authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        creds.getEmail(),
                                                        creds.getPassword(),
                                                        new ArrayList<>()));
                } catch (IOException e) {
                        LOGGER.error("The user cannot be authenticated", e);
                        throw new UnableToAuthenticateUserException();
                }
        }

        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                        FilterChain chain,
                        Authentication auth) throws IOException, ServletException {
                String token = JWT.create()
                                .withSubject(((User) auth.getPrincipal()).getUsername())
                                .withClaim("role", auth.getAuthorities().iterator().next().getAuthority())
                                .withExpiresAt(new Date(System.currentTimeMillis()
                                                + AuthenticationConfigConstants.EXPIRATION_TIME))
                                .sign(Algorithm.HMAC512(AuthenticationConfigConstants.SECRET.getBytes()));

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter()
                                .write(new StringBuilder().append("{\"")
                                                .append(AuthenticationConfigConstants.HEADER_STRING)
                                                .append("\":\"").append(AuthenticationConfigConstants.TOKEN_PREFIX)
                                                .append(token).append("\"}")
                                                .toString());
                response.addHeader(AuthenticationConfigConstants.HEADER_STRING,
                                AuthenticationConfigConstants.TOKEN_PREFIX + token);
        }
}