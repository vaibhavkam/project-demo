/**
 * 
 */
package com.invoice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author vkamble
 *
 */
@Configuration
@EnableWebSecurity
public class InvoiceServiceSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests().antMatchers("/**")
		.hasRole("USER").and().csrf().disable();
	}
	
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication().withUser(env.getProperty("validConsumerUserName")).password(env.getProperty("validConsumerPassword")).roles("USER");
	}
}
