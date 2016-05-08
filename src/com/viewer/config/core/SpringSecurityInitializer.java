package com.viewer.config.core;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import com.viewer.config.SpringSecurityConfig;

public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
	public SpringSecurityInitializer() {
		super(SpringSecurityConfig.class);
	}
}
