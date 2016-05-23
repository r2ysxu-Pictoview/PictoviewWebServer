package com.viewer.config.core;

import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.multipart.support.MultipartFilter;

import com.viewer.config.SpringSecurityConfig;

public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
	public SpringSecurityInitializer() {
		super(SpringSecurityConfig.class);
	}
	
	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
		insertFilters(servletContext, new MultipartFilter());
	}
}
