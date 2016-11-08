package com.jlccaires.sourcemanager.security;

import com.jlccaires.sourcemanager.domain.ServerDetails;
import com.jlccaires.sourcemanager.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.StringUtils;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableRedisHttpSession
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ServerService serverService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().cacheControl().disable().and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/auth")
                .successForwardUrl("/")
                .usernameParameter("port")
                .passwordParameter("rconPassword")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and().csrf().disable()
                .exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(HttpStatus.UNAUTHORIZED.value()));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authentication -> {
            final int port = StringUtils.isEmpty(authentication.getName()) ? 27015 : Integer.parseInt(authentication.getName());
            final String rconPassword = authentication.getCredentials().toString();

            try {
                if (!serverService.auth(port, rconPassword)) {
                    throw new BadCredentialsException("Invalid username or password!");
                }

                return new UsernamePasswordAuthenticationToken(new ServerDetails(port, rconPassword), null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

    }
}