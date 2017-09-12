/**
 * 
 */
package com.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author vkamble
 *
 */
@SpringBootApplication
public class Boot extends SpringBootServletInitializer{

	public static void main(String[] args) {
        SpringApplication.run(Boot.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Boot.class);
	}

}
