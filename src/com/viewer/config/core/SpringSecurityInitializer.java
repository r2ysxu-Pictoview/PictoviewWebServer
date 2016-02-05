package com.viewer.config.core;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import com.viewer.config.SecurityConfig;

public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
	public SpringSecurityInitializer() {
		super(SecurityConfig.class);
	}
}
