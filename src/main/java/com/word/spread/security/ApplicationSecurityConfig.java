package com.word.spread.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@KeycloakConfiguration
public class ApplicationSecurityConfig extends KeycloakWebSecurityConfigurerAdapter
{

    @Bean
    AuthenticationProvider authenticationProvider() {
    	KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
    	SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
    	grantedAuthorityMapper.setPrefix("ROLE_");
//         grantedAuthorityMapper.setConvertToUpperCase(true);
    	keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
    	
    	return keycloakAuthenticationProvider;
    }
    /**
     * Defines the session authentication strategy.
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(buildSessionRegistry());
    }

    @Bean
    protected SessionRegistry buildSessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        super.configure(http);
        http
        	.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/words").hasRole("user")
                .anyRequest().permitAll();
    }
	
	//https://www.keycloak.org/docs/latest/securing_apps/index.html#_spring_security_adapter
	@Bean
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
	    return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	}
	
    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }


//	@Override
//	protected void configure(HttpSecurity http) throws Exception {

		
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
//		Filter keycloakAuthenticationProcessingFilter = new KeycloakAuthenticationProcessingFilter(authenticationManager());
//		http.addFilterAfter(keycloakAuthenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class);
		
//		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users").permitAll();
//		http
//			.cors().disable()
//			.csrf().disable()
//			.authorizeRequests()
//			.antMatchers(HttpMethod.GET, "/api/words").permitAll().antMatchers("/api/*")
//			.hasAnyRole("user", "WEB_USER")
//		.anyRequest().permitAll();
		
//		super.configure(http);
//		http.oauth2Login().and().logout().addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/");
//	}

	// temporary disable cors
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
//		configuration.setAllowedMethods(Arrays.asList("*"));
//		configuration.setAllowCredentials(true);
//		configuration.setAllowedHeaders(Arrays.asList("*"));
//
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}

//	@Bean
//	public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
//			KeycloakAuthenticationProcessingFilter filter) {
//		FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
//		registrationBean.setEnabled(false);
//		return registrationBean;
//	}
//
//	@Bean
//	public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(KeycloakPreAuthActionsFilter filter) {
//		FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
//		registrationBean.setEnabled(false);
//		return registrationBean;
//	}
//
//	@Bean
//	public FilterRegistrationBean keycloakAuthenticatedActionsFilterBean(KeycloakAuthenticatedActionsFilter filter) {
//		FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
//		registrationBean.setEnabled(false);
//		return registrationBean;
//	}
//
//	@Bean
//	public FilterRegistrationBean keycloakSecurityContextRequestFilterBean(
//			KeycloakSecurityContextRequestFilter filter) {
//		FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
//		registrationBean.setEnabled(false);
//		return registrationBean;
//	}
}
