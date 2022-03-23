package com.bancocaminos.impactbackendapi.config;

import com.bancocaminos.impactbackendapi.authentication.filter.JWTBasicAuthenticationFilter;
import com.bancocaminos.impactbackendapi.authentication.filter.JWTUsernamePasswordAuthenticationFilter;
import com.bancocaminos.impactbackendapi.authentication.service.AuthenticationUserDetailService;
import com.bancocaminos.impactbackendapi.core.constants.GenericApiConstants;
import com.bancocaminos.impactbackendapi.twofa.security.CustomAuthenticationProvider;
import com.bancocaminos.impactbackendapi.user.rest.model.Role;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN = "/login";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final AuthenticationUserDetailService authenticationUserDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().maximumSessions(1).expiredUrl(LOGIN);
        http.csrf().disable().authorizeRequests()
                .antMatchers(GenericApiConstants.User.COMPLETE_USER_API).permitAll()
                .antMatchers(GenericApiConstants.SECOND_FACTOR_COMPLETE_API).permitAll()
                .antMatchers(GenericApiConstants.ClientCampaignResult.CLIENT_CAMPAIGN_RESULTS_COMPLETE_API)
                .hasAnyAuthority(Role.ADMIN.name())
                .and()
                .addFilter(new JWTUsernamePasswordAuthenticationFilter(authenticationManager(),
                        customAuthenticationProvider))
                .addFilter(new JWTBasicAuthenticationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationUserDetailService).passwordEncoder(bCryptPasswordEncoder);
    }
}
